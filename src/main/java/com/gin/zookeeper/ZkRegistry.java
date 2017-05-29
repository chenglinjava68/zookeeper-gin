package com.gin.zookeeper;

import com.alibaba.fastjson.JSON;
import com.gin.zookeeper.constants.RegistryConstants;
import com.gin.zookeeper.listener.ZkNotifyListener;
import com.gin.zookeeper.pojo.ProviderInfo;
import com.gin.zookeeper.pojo.ServiceInfo;
import com.gin.zookeeper.utils.ProviderInfoUtils;
import com.google.common.base.Joiner;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.retry.BoundedExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ZkRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkRegistry.class);

    private static final String END_PATH_PREFIX_TEMPLATE = "ip=%s&port=%s";
    private static final String END_PATH_TEMPLATE = END_PATH_PREFIX_TEMPLATE + "&weight=%s&status=%s";

    private CuratorFramework zkClient;

    public ZkRegistry(ZkConfig zkConfig) {
        zkClient = CuratorFrameworkFactory.builder().connectString(zkConfig.getZkAddress())
                .sessionTimeoutMs(zkConfig.getZkTimeout())
                .retryPolicy(new BoundedExponentialBackoffRetry(zkConfig.getBaseSleepTimeMs(),
                        zkConfig.getMaxSleepTimeMs(), zkConfig.getMaxRetries()))
                .build();

        zkClient.start();
        LOGGER.info("zkClient start:{}", JSON.toJSONString(zkConfig));
    }

    public void register(ServiceInfo info, ProviderInfo providerInfo, String serviceName, String role) throws Exception {
        if (info == null || providerInfo == null) {
            throw new IllegalArgumentException("register info param invalid");
        }

        if (info.getInterfaceClazz() == null || StringUtils.isBlank(role) || StringUtils.isBlank(serviceName)) {
            throw new IllegalArgumentException("register info.clazz or role or serviceName param invalid");
        }

        String parentPath = Joiner.on(RegistryConstants.PATH_SEPARATOR).join(RegistryConstants.PATH_SEPARATOR+serviceName, adjustClazzName(info),
                info.getGroup(), info.getVersion(), role);
        String endPath = String.format(END_PATH_TEMPLATE, providerInfo.getIp(), providerInfo.getPort(),
                providerInfo.getWeight(), providerInfo.isStatus());
        String path = parentPath + RegistryConstants.PATH_SEPARATOR + endPath;

        try {
            unregister(info,providerInfo,role, serviceName);

            zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path);
            LOGGER.info("register {} service in path :{}", serviceName, path);
        } catch (Throwable e) {
            LOGGER.error("register {} service error.path:{}, e:{}", serviceName, path, e.getMessage(), e);
            throw new Exception("register " + serviceName + " service error:" + e.getMessage());
        }
    }

    public void unregister(ServiceInfo info, ProviderInfo providerInfo, String serviceName, String role) {
        String parentPath = Joiner.on(RegistryConstants.PATH_SEPARATOR).join(RegistryConstants.PATH_SEPARATOR+serviceName, adjustClazzName(info),
                info.getGroup(), info.getVersion(), role);
        String endPrefixPath = String.format(END_PATH_PREFIX_TEMPLATE, providerInfo.getIp(), providerInfo.getPort());

        try {
            // 由于状态可能会变更导致节点变化,所以这里先获取list,然后找到对应服务的前缀节点处理
            List<String> nodes = zkClient.getChildren().forPath(parentPath);

            for (String endPath : nodes) {
                // 只会有一个临时节点存在
                if (endPath.startsWith(endPrefixPath)) {
                    zkClient.delete().forPath(parentPath + RegistryConstants.PATH_SEPARATOR + endPath);
                    break;
                }
            }
        } catch (Throwable e) {
            LOGGER.error("unregister {} service error.parentPath:{}, endPrefixPath:{}, e:{}", serviceName, parentPath, endPrefixPath,
                    e.getMessage());
        }
    }

    public void subscribe(ServiceInfo info, final ZkNotifyListener listener, String serviceName) throws  Exception {
        final String parentPath = Joiner.on(RegistryConstants.PATH_SEPARATOR).join(RegistryConstants.PATH_SEPARATOR+serviceName,
                adjustClazzName(info), info.getGroup(), info.getVersion(), RegistryConstants.DEFAULT_INVOKER_PROVIDER);

        try {
            List<String> nodes = zkClient.getChildren().usingWatcher(new CuratorWatcher() {

                @Override
                public void process(WatchedEvent event) throws Exception {
                    List<String> childrenNodes = zkClient.getChildren().usingWatcher(this).forPath(parentPath);
                    listener.notify(ProviderInfoUtils.convertZkChildren(childrenNodes));
                }

            }).forPath(parentPath);

            listener.notify(ProviderInfoUtils.convertZkChildren(nodes));
        } catch (Throwable e) {
            LOGGER.error("subscribe {} service error.path:{}, e:{}", serviceName, parentPath, e.getMessage(), e);
            throw new Exception("subscribe " + serviceName + " service error:" + e.getMessage());
        }

        // 主动拉取数据,防止zk监听失效
        Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> nodes = zkClient.getChildren().forPath(parentPath);
                    listener.notify(ProviderInfoUtils.convertZkChildren(nodes));
                } catch (Exception e) {
                    LOGGER.warn("pull zookeeper path:{} children fail.e:{}", parentPath, e.getMessage(), e);
                }
            }
        },1L, 30L, TimeUnit.SECONDS);
    }

    private String adjustClazzName(ServiceInfo info) {
        String clazzName = info.getInterfaceClazz().getCanonicalName();
        return clazzName;
    }

}
