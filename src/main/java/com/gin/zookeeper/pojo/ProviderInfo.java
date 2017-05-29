package com.gin.zookeeper.pojo;

import java.io.Serializable;

/**
 * 服务的地址,端口,权重
 */
public class ProviderInfo implements Serializable {
    private static final long serialVersionUID = 733316025823163238L;
    /**
     * IP地址
     */
    private String ip;
    /**
     * 端口
     */
    private int port;
    /**
     * 权重
     */
    private int weight;
    /**
     * 是否对外提供服务
     */
    private boolean status = true;

    public ProviderInfo(){}

    public ProviderInfo(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProviderInfo that = (ProviderInfo) o;

        if (port != that.port) return false;
        if (ip != null ? !ip.equals(that.ip) : that.ip != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ip != null ? ip.hashCode() : 0;
        result = 31 * result + port;
        return result;
    }

    @Override
    public String toString() {
        return "ProviderInfo{" +
                "ip='" + ip + '\'' +
                ", port=" + port +
                ", weight=" + weight +
                ", status=" + status +
                '}';
    }

}
