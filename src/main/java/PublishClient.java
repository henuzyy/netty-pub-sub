import codec.MessageCodec;
import handler.ExceptionHandler;
import handler.ServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.apache.log4j.Logger;
import service.ServiceApplication;

/**
 * 发布订阅的服务
 * Created by zyy on 2016/6/12.
 */
public class PublishClient {

    private Logger logger = Logger.getLogger(this.getClass());

    private static EventLoopGroup bossGroup = null;  //主线程池

    private static EventLoopGroup workerGroup = null; //工作线程池


    private static boolean isOpen = false;  //是否开启

    private PublishClient() {
    }

    private static class ServerHolder {
        private final static PublishClient INSTANCE = new PublishClient();
    }

    public static final PublishClient getInstance() {
        return ServerHolder.INSTANCE;
    }

    public void start() {
        if (isOpen) {
            logger.info("服务已经启动！");
            return;
        }
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, false)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new ObjectDecoder(1024*1024,
                                ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                        pipeline.addLast(new ObjectEncoder());
//                        pipeline.addLast("codec",new MessageCodec());
                        pipeline.addLast("readTimeout",new ReadTimeoutHandler(30));
                        pipeline.addLast("Exception",new ExceptionHandler());
                        pipeline.addLast("service",new ServerHandler());
                    }
                });
        try {
            ChannelFuture future = bootstrap.bind(9997).sync(); //绑定9997端口
            isOpen = true;
            logger.info("发布程序已经启动！");
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("bind error", e);
        }
    }

    public void stop() {
        if (isOpen) {
            if (bossGroup != null && !bossGroup.isShuttingDown()) {
                bossGroup.shutdownGracefully();
            }
            if (workerGroup != null && !workerGroup.isShuttingDown()) {
                workerGroup.shutdownGracefully();
            }
        }
    }


    public static void main(String[] args) {
        ServiceApplication.initial();  //初始化application
        PublishClient server = PublishClient.getInstance();
        server.start();


    }

}
