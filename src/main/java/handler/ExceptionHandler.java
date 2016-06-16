package handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.apache.log4j.Logger;


/**
 * Created by zyy on 2016/6/16.
 */
public class ExceptionHandler extends ChannelDuplexHandler {

    private Logger logger = Logger.getLogger(ExceptionHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("出现异常，信息为:",cause);
    }
}
