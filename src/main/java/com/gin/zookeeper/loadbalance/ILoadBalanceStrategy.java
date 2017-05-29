package com.gin.zookeeper.loadbalance;

import com.gin.zookeeper.pojo.Invocation;
import com.gin.zookeeper.pojo.InvokeConn;

import java.util.List;

public interface ILoadBalanceStrategy {

    /**
     * 从众多连接池子中选择其中一个池子
     * @param invokeConns
     * @param invocation
     * @return
     */
    public InvokeConn select(List<InvokeConn> invokeConns, Invocation invocation);

}
