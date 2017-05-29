package com.gin.zookeeper.loadbalance;

import com.gin.zookeeper.pojo.Invocation;
import com.gin.zookeeper.pojo.InvokeConn;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class RoundRobinLoadBalanceStrategy extends AbstractLoadBalanceStrategy {

    private static final ConcurrentHashMap<String, Integer> current = new ConcurrentHashMap<String, Integer>();

    @Override
    protected InvokeConn doSelect(List<InvokeConn> invokeConns, Invocation invocation) {
        String key = null;
        if (invocation == null) {
            key = "default_key";
        }else {
            key = invocation.getInterfaceName();
        }

        Integer cur = current.get(key);

        if (cur == null || cur >= Integer.MAX_VALUE - 1) {
            cur = 0;
        }
        current.putIfAbsent(key, cur + 1);

        try {
            return invokeConns.get(cur % invokeConns.size());
        }catch (IndexOutOfBoundsException e){
            return invokeConns.get(0);
        }

    }

}
