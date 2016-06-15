package service;

import io.netty.channel.group.ChannelGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务容器
 * Created by zyy on 2016/6/12.
 */
public class ServiceApplication {

    //serivce集合
    public static Map<Integer, Service> serviceMap = new ConcurrentHashMap<Integer, Service>();

    //根据topic 订阅的客户端  <Topic，ChannelGroup>
    public static Map<String, ChannelGroup> subTopicMap = new ConcurrentHashMap<String, ChannelGroup>();

/*    static{
        PubService pubService = new PubService();
        serviceMap.put(pubService.getCMD(),pubService);
        SubService subService = new SubService();
        serviceMap.put(subService.getCMD(),subService);
    }*/

    public static Service getService(Integer num) {
        return serviceMap.get(num);
    }

    public static void initial() {
        PubService pubService = new PubService();
        serviceMap.put(pubService.getCMD(), pubService);
        SubService subService = new SubService();
        serviceMap.put(subService.getCMD(), subService);
    }

//    public
}
