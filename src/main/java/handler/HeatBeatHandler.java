package handler;

import beans.Protocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.ScheduledFuture;

import java.util.concurrent.TimeUnit;

/**
 * Created by zyy on 2016/6/16.
 */
public class HeatBeatHandler extends ChannelInboundHandlerAdapter {


    private volatile ScheduledFuture<?> heatFuture;

    private class HeatBeatTask implements Runnable {

        private final ChannelHandlerContext ctx;

        public HeatBeatTask(final ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        public void run() {
            Protocol protocol = new Protocol();
            System.out.println("发一次心跳检测");
            protocol.setCMD((short) 5);  //心跳
            ctx.writeAndFlush(protocol);
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //订阅
        Protocol p = new Protocol();
        p.setCMD((short) 3);
        ctx.writeAndFlush(p);
        //10s定时心跳检测
        heatFuture = ctx.executor().scheduleAtFixedRate(new HeatBeatHandler.HeatBeatTask(ctx),
                0, 10, TimeUnit.SECONDS);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (heatFuture != null) {
            heatFuture.cancel(true);
            heatFuture = null;
        }
        ctx.fireExceptionCaught(cause);
    }


}
