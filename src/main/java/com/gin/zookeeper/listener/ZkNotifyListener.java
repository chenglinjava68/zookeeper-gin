package com.gin.zookeeper.listener;

import com.gin.zookeeper.pojo.ProviderInfo;

import java.util.Set;

public interface ZkNotifyListener {

    /**
     * 根据监听到得变更获取最新的服务提供者的列表
     *
     * @param providerInfos
     */
    void notify(Set<ProviderInfo> providerInfos);

}
