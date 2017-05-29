package com.gin.zookeeper.pojo;

import java.io.Serializable;

/**
 * 服务信息id+interfaceName+version+group唯一确定一个调用服务
 */
public class ServiceInfo implements Serializable {
    private static final long serialVersionUID = -7572772909108866010L;
    /**
     * 服务接口名,全路径
     */
    private Class interfaceClazz;
    /**
     * 服务版本号
     */
    private String version;
    /**
     * 服务集群组名,如果服务部署两套环境,推荐设置该参数
     */
    private String group;
    /**
     * 表示服务是否只是直接调用.true表示直接调用,false表示会额外注册到zk上
     */
    private boolean directInvoke;

    public Class getInterfaceClazz() {
        return interfaceClazz;
    }

    public void setInterfaceClazz(Class interfaceClazz) {
        this.interfaceClazz = interfaceClazz;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isDirectInvoke() {
        return directInvoke;
    }

    public void setDirectInvoke(boolean directInvoke) {
        this.directInvoke = directInvoke;
    }

    @Override
    public String toString() {
        return "ServiceInfo{" +
                "interfaceClazz=" + interfaceClazz +
                ", version='" + version + '\'' +
                ", group='" + group + '\'' +
                ", directInvoke=" + directInvoke +
                '}';
    }

}
