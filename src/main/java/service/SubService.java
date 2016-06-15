package service;

import beans.Protocol;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Set;

/**
 * 订阅service
 * Created by zyy on 2016/6/12.
 */
public class SubService implements Service {

    private Logger logger = Logger.getLogger(SubService.class);

    private int CMD = 3;     //命令序号
    private int CMD_REP = 4; //回复

    public void execute(Channel ch, Protocol protocol) throws Exception {
        if (protocol != null) {
            // topic topic
            String topic = new String(protocol.getMsg());
            if (topic == null || topic.length() <= 0)
                throw new RuntimeException("订阅参数出现异常！");
            if (ServiceApplication.subTopicMap.containsKey(topic)) {
                ChannelGroup channelGroup = ServiceApplication.subTopicMap.get(topic);
                channelGroup.add(ch);
            } else {
                ChannelGroup channelGroup = new DefaultChannelGroup(topic, GlobalEventExecutor.INSTANCE);
                channelGroup.add(ch);
                ServiceApplication.subTopicMap.put(topic, channelGroup);
            }
        }

        Set<Map.Entry<String, ChannelGroup>> sets = ServiceApplication.subTopicMap.entrySet();
        for (Map.Entry<String, ChannelGroup> item : sets) {
            logger.info(String.format("topic: %s   ;   sub this topic userSize=%s", item.getKey(), item.getValue().size()));
        }

    }

    public int getCMD() {
        return CMD;
    }

    public void setCMD(int CMD) {
        this.CMD = CMD;
    }

    public int getCMD_REP() {
        return CMD_REP;
    }

    public void setCMD_REP(int CMD_REP) {
        this.CMD_REP = CMD_REP;
    }
}
