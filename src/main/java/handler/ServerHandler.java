package handler;

import beans.Protocol;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import service.Service;
import service.ServiceApplication;

/**
 * Created by zyy on 2016/6/12.
 */
public class ServerHandler extends SimpleChannelInboundHandler<Protocol> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Protocol protocol) throws Exception {
        Service service = ServiceApplication.getService((int) protocol.getCMD());
        service.execute(channelHandlerContext.channel(), protocol);
    }
}
