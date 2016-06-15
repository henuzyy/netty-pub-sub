import codec.MessageCodec;
import handler.SubHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.log4j.Logger;

/**
 * 订阅某个主题的客户端
 * Created by zyy on 2016/6/12.
 */
public class SubClient {

    private Logger logger = Logger.getLogger(this.getClass());

    private final static String HOST = "127.0.0.1";

    private final static int PORT = 9997;

    private EventLoopGroup group;

    /*
    *
    * */
    public void connect(final String topic) {

        group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new ObjectDecoder(1024*1024,
                                ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                        pipeline.addLast(new ObjectEncoder());
//                        pipeline.addLast(new MessageCodec());
                        pipeline.addLast(new SubHandler(topic));
                    }
                });
        try {
            ChannelFuture f = bootstrap.connect(this.HOST, this.PORT).sync();
            logger.info("订阅者客户端启动啦");
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SubClient().connect("topic"); //订阅topic为 hello的主题
    }
}
