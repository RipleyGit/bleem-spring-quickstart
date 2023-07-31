package site.bleem.boot.socket.channel;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.net.InetSocketAddress;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.bleem.boot.socket.data.OriMessageEntity;
import site.bleem.boot.socket.enums.ConnectProtocolType;

public class UDPMessageDecoder extends MessageToMessageDecoder<DatagramPacket> {
    private static final Logger log = LoggerFactory.getLogger(UDPMessageDecoder.class);
    private static final Logger logger = LoggerFactory.getLogger("sourcelog");

    public UDPMessageDecoder() {
    }

    protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> list) {
        ByteBuf byteBuf = (ByteBuf)datagramPacket.content();
        logger.info("UDP接收<-address:[{}]原始数据[{}]", ((InetSocketAddress)datagramPacket.sender()).toString(), ByteBufUtil.hexDump(byteBuf));
        ByteBufUtil.getBytes(byteBuf);
        if (byteBuf.getByte(0) == -1) {
            if (byteBuf.getByte(byteBuf.writerIndex() - 1) == -2) {
                OriMessageEntity oriMessage = new OriMessageEntity();
                oriMessage.setOriData(Unpooled.wrappedBuffer(ByteBufUtil.getBytes(byteBuf)));
                oriMessage.setProtocolType(ConnectProtocolType.Udp);
                oriMessage.setAddress(((InetSocketAddress)datagramPacket.sender()).getAddress().getHostAddress());
                oriMessage.setPort(((InetSocketAddress)datagramPacket.sender()).getPort());
                list.add(oriMessage);
            }
        }
    }
}
