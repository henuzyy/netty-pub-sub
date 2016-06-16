import beans.PriceData;
import beans.Protocol;
import codec.MessageCodec;
import com.alibaba.fastjson.JSONObject;
import handler.DataSourceHandler;
import handler.ExceptionHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.log4j.Logger;
import util.StringUtil;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 数据源客户端
 * Created by zyy on 2016/6/12.
 */
public class DataSourceclient implements Runnable {


    private Logger logger = Logger.getLogger(this.getClass());

    private final static String HOST = "127.0.0.1";

    private final static int PORT = 9997;

    private EventLoopGroup group;


    public static void main(String[] args) {
        ExecutorService service = Executors.newFixedThreadPool(1);
        service.execute(new DataSourceclient());
    }

    public void run() {
        group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new ObjectDecoder(1024 * 1024,
                                ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                        pipeline.addLast(new ObjectEncoder());
//                        pipeline.addLast(new MessageCodec());
                        pipeline.addLast("Exception",new ExceptionHandler());
                    }
                });
        try {
            ChannelFuture f = bootstrap.connect(this.HOST, this.PORT).sync();
            logger.info("数据源客户端启动啦");
            Channel channel = f.channel();
            while (true) {
                //向某个topic 发布某条消息
                Protocol protocol = new Protocol();
                protocol.setCMD((short) 1);
                String msg = JSONObject.toJSONString(this.createPriceData());
//                    String msg = this.topic + StringUtil.getLineDelimiter() + this.topicMsg + i;
                logger.info("要发送的消息内容为：" + msg);
                protocol.setMsg(msg.getBytes());
                channel.writeAndFlush(protocol);
                //休眠1s
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            if (group != null && !group.isShuttingDown()) {
                group.shutdownGracefully();
            }
        }
    }

    /**
     * 随机生成行情数据
     */
    public PriceData createPriceData() {
        Random r = new Random();
        return new PriceData(String.valueOf(r.nextInt(99999)), "stock" + r.nextInt(100), r.nextFloat());
    }
}
