package com.gin.zookeeper.utils;

import java.net.Inet4Address;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class LocalIpUtils {

    /**
     * 获取本地ip地址,如果多个,选择第一个
     *
     * @return
     */
    public static String getLocalIp() {
        try {
            for (Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements(); ) {
                NetworkInterface item = e.nextElement();
                for (InterfaceAddress address : item.getInterfaceAddresses()) {
                    if (address.getAddress() instanceof Inet4Address) {
                        Inet4Address inet4Address = (Inet4Address) address.getAddress();
                        if (inet4Address.isLoopbackAddress()) {
                            continue;
                        }
                        return inet4Address.getHostAddress();
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("no local ip");
    }

}
