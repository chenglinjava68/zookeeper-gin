package com.gin.zookeeper.pojo;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class InvokeConn implements Serializable {
    private static final long serialVersionUID = -805739143582019252L;
    /**
     * 服务端的信息ip+port
     */
    private ProviderInfo providerInfo;

    public InvokeConn(ProviderInfo providerInfo) {
        this.providerInfo = providerInfo;
    }

    public ProviderInfo getProviderInfo() {
        return providerInfo;
    }

    public void setProviderInfo(ProviderInfo providerInfo) {
        this.providerInfo = providerInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        InvokeConn conn = (InvokeConn) o;

        return new EqualsBuilder()
                .append(providerInfo, conn.providerInfo)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(providerInfo)
                .toHashCode();
    }

}
