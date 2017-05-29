package com.gin.zookeeper;

public class ZkConfig {
    /**
     * zk ip:port
     */
    private String zkAddress;
    /**
     * zk 超时时间
     */
    private int zkTimeout = 3000;
    /**
     * 重试之间初始等待时间
     */
    private int baseSleepTimeMs = 10;
    /**
     * 重试之间最长等待时间
     */
    private int maxSleepTimeMs = 1000;
    /**
     * 重试次数
     */
    private int maxRetries = 3;

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public int getZkTimeout() {
        return zkTimeout;
    }

    public void setZkTimeout(int zkTimeout) {
        this.zkTimeout = zkTimeout;
    }

    public int getBaseSleepTimeMs() {
        return baseSleepTimeMs;
    }

    public void setBaseSleepTimeMs(int baseSleepTimeMs) {
        this.baseSleepTimeMs = baseSleepTimeMs;
    }

    public int getMaxSleepTimeMs() {
        return maxSleepTimeMs;
    }

    public void setMaxSleepTimeMs(int maxSleepTimeMs) {
        this.maxSleepTimeMs = maxSleepTimeMs;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    @Override
    public String toString() {
        return "ZkConfig{" +
                "zkAddress='" + zkAddress + '\'' +
                ", zkTimeout=" + zkTimeout +
                ", baseSleepTimeMs=" + baseSleepTimeMs +
                ", maxSleepTimeMs=" + maxSleepTimeMs +
                ", maxRetries=" + maxRetries +
                '}';
    }

}
