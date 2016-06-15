import codec.MessageCodec;
import handler.PubHandler;
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
 * 发布某个主题的客户端
 * Created by zyy on 2016/6/12.
 */
public class PubClient {


    private Logger logger = Logger.getLogger(this.getClass());

    private final static String HOST = "127.0.0.1";

    private final static int PORT = 9997;

    private EventLoopGroup group;

    public void conncet(final String topic,final String topicMsg) {

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
                        pipeline.addLast(new PubHandler(topic,topicMsg));
                    }
                });
        try {
            ChannelFuture f = bootstrap.connect(this.HOST, this.PORT).sync();
            logger.info("发布者客户端启动啦");
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    public static void main(String[] args) {
        new PubClient().conncet("topic","这是订阅需要的内容");
    }

}
