package codec;

import beans.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * 编解码器<br/>
 * 注意：编解码的顺序要一致
 * <p/>
 * Created by zyy on 2016/6/12.
 */
public class MessageCodec extends MessageToMessageCodec<ByteBuf, Protocol> {

    private Logger logger = Logger.getLogger(MessageCodec.class);

    /**
     * 编码器
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Protocol protocol, List<Object> list) throws Exception {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
        if (protocol.getMsg() != null) {
            byteBuf.writeInt(Protocol.HEADER_LENGTH + protocol.getMsg().length);
            byteBuf.writeInt(Protocol.HEADER_LENGTH);
            byteBuf.writeShort(protocol.getVersion());
            byteBuf.writeShort(protocol.getCMD());
            byteBuf.writeBytes(protocol.getMsg());
        } else {
            byteBuf.writeInt(Protocol.HEADER_LENGTH);
            byteBuf.writeInt(Protocol.HEADER_LENGTH);
            byteBuf.writeShort(protocol.getVersion());
            byteBuf.writeShort(protocol.getCMD());
        }
        logger.info("encode:" + protocol);
        list.add(byteBuf);
    }

    /**
     * 解码器
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) {
        int length = byteBuf.readableBytes();
        if (length < 4) {
            return;
        }
        byteBuf.markReaderIndex();
        int packageLength = byteBuf.readInt();
        if (packageLength - 4 < byteBuf.readableBytes()) {
            byteBuf.resetReaderIndex();
            return;
        }
        Protocol protocol = new Protocol();
        protocol.setPackageLength(packageLength);
        protocol.setHeaderLength(byteBuf.readInt());
        protocol.setVersion(byteBuf.readShort());
        protocol.setCMD(byteBuf.readShort());
        if (protocol.getPackageLength() > protocol.getHeaderLength()) { //有包体内容，心跳检测之类的可以无包体内容
            byte[] msg = new byte[protocol.getPackageLength() - protocol.getHeaderLength()];
            byteBuf.readBytes(msg);
            protocol.setMsg(msg);
        }
        logger.info("decode:" + protocol);
        list.add(protocol);
    }
}
