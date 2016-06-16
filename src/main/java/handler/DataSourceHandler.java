package handler;

import beans.Protocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;
import util.StringUtil;

/**
 * Created by zyy on 2016/6/12.
 */
public class DataSourceHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = Logger.getLogger(DataSourceHandler.class);

    private String topic;

    private String topicMsg;


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //向某个topic 发布某条消息
        Protocol protocol = new Protocol();


        protocol.setCMD((short) 1);
        while(true)
        for (int i = 0; i < 10; i++) {
            String msg = this.topic+ StringUtil.getLineDelimiter() + this.topicMsg +i;
            logger.info("要发送的消息内容为：" + msg);
            protocol.setMsg(msg.getBytes());
            ctx.writeAndFlush(protocol);
        }



    }

    public DataSourceHandler() {
    }

    public DataSourceHandler(String topic, String topicMsg) {
        this.topic = topic;
        this.topicMsg = topicMsg;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTopicMsg() {
        return topicMsg;
    }

    public void setTopicMsg(String topicMsg) {
        this.topicMsg = topicMsg;
    }
}
