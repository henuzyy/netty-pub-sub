package handler;

import beans.PriceData;
import beans.Protocol;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //如果有发布topic，就会收到消息
        Protocol protocol = (Protocol) msg;
//        logger.info(protocol);
        if (protocol.getMsg() != null) {
            PriceData data = JSONObject.parseObject(new String(protocol.getMsg()), PriceData.class);
            logger.info(String.format("receive msg :%s", data.toString()));
        }
    }


    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj)
            throws Exception {
        if (obj instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) obj;
            //规定时间未接收到client发来的消息，主动关闭该链接
            if (event.state() == IdleState.READER_IDLE) {
                logger.error("读超时");
                ctx.close();
            } else if (event.state() == IdleState.WRITER_IDLE) {
                logger.error("写超时");
            } else if (event.state() == IdleState.ALL_IDLE) {
                logger.error("全部超时");
            }
        }
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
