package com.gin.zookeeper.loadbalance;

import com.gin.zookeeper.pojo.Invocation;
import com.gin.zookeeper.pojo.InvokeConn;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public abstract class AbstractLoadBalanceStrategy implements ILoadBalanceStrategy {

    @Override
    public InvokeConn select(List<InvokeConn> invokeConns, Invocation invocation) {
        if (CollectionUtils.isEmpty(invokeConns)) {
            throw new RuntimeException("no valid provider exist online");
        }

        if (invokeConns.size() == 1) {
            return invokeConns.get(0);
        }

        return doSelect(invokeConns, invocation);
    }

    protected abstract InvokeConn doSelect(List<InvokeConn> invokeConns, Invocation invocation);

}
