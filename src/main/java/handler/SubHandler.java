package handler;

import beans.Protocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.log4j.Logger;

/**
 * Created by zyy on 2016/6/12.
 */
public class SubHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = Logger.getLogger(SubHandler.class);

    private String topic;

    public SubHandler() {
    }

    public SubHandler(String topic) {
        this.topic = topic;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //订阅某个topic
        Protocol p = new Protocol();
        p.setCMD((short) 3);
        p.setMsg(this.topic.getBytes());
        ctx.writeAndFlush(p);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //如果有发布topic，就会收到消息
        Protocol protocol = (Protocol) msg;
//        logger.info(protocol);
        logger.info(String.format("receive msg :%s", new String(protocol.getMsg())));
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
