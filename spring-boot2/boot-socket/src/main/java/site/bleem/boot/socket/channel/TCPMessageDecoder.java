package site.bleem.boot.socket.channel;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.bleem.boot.socket.container.ContainerManager;
import site.bleem.boot.socket.data.OriMessageEntity;
import site.bleem.boot.socket.enums.ConnectProtocolType;
import site.bleem.boot.socket.utils.SpringUtil;

public class TCPMessageDecoder extends ByteToMessageDecoder {
    private static final Logger log = LoggerFactory.getLogger(TCPMessageDecoder.class);
    private ContainerManager containerManager = (ContainerManager) SpringUtil.getBean(ContainerManager.class);
    private int frameHeaderBufIndex = -1;
    private int frameFooterBufIndex = -1;
    private static final Logger logger = LoggerFactory.getLogger("sourcelog");

    public TCPMessageDecoder() {
    }

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        logger.info("TCP接收<-address:[{}]原始数据[{}]", channelHandlerContext.channel().remoteAddress().toString(), ByteBufUtil.hexDump(byteBuf));
        if (byteBuf.readableBytes() > 20000) {
            log.info("数据长度超过默认最大值{},丢弃", 20000);
            byteBuf.clear();
        } else {
            this.loopCheckData(channelHandlerContext.channel().remoteAddress(), byteBuf, list);
        }
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (ctx.channel().remoteAddress() != null) {
            log.info("socket客户端连接:{}", ctx.channel().remoteAddress().toString().substring(1));
        }

    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        try {
            log.info("socket客户端链接关闭:{}", ctx.channel());
        } catch (Exception var3) {
            log.error("channelInactive", var3);
        }

    }

    private boolean findFrameHeader(ByteBuf byteBuf) {
        while(byteBuf.isReadable()) {
            byte readByte = byteBuf.getByte(byteBuf.readerIndex());
            if (readByte == -1) {
                this.frameHeaderBufIndex = byteBuf.readerIndex();
                byteBuf.markReaderIndex();
                byteBuf.readByte();
                return true;
            }

            byteBuf.readByte();
        }

        return false;
    }

    private boolean hasInactData() {
        return this.frameHeaderBufIndex >= 0 && this.frameFooterBufIndex > 0 && this.frameHeaderBufIndex < this.frameFooterBufIndex;
    }

    private boolean findFrameFooter(ByteBuf byteBuf) {
        while(byteBuf.isReadable()) {
            byte readByte = byteBuf.getByte(byteBuf.readerIndex());
            if (readByte == -2) {
                this.frameFooterBufIndex = byteBuf.readerIndex();
                byteBuf.readByte();
                return true;
            }

            if (readByte == -1) {
                this.frameHeaderBufIndex = byteBuf.readerIndex();
                byteBuf.readByte();
            } else {
                byteBuf.readByte();
            }
        }

        byteBuf.resetReaderIndex();
        return false;
    }

    private boolean loopCheckData(SocketAddress clientAddress, ByteBuf byteBuf, List<Object> list) {
        if (!this.findFrameHeader(byteBuf)) {
            return false;
        } else if (!this.findFrameFooter(byteBuf)) {
            return false;
        } else if (!this.hasInactData()) {
            return false;
        } else {
            OriMessageEntity oriMessage = new OriMessageEntity();
            byte[] copyData = ByteBufUtil.getBytes(byteBuf, this.frameHeaderBufIndex, this.frameFooterBufIndex - this.frameHeaderBufIndex + 1);
            oriMessage.setOriData(Unpooled.wrappedBuffer(copyData));
            oriMessage.setProtocolType(ConnectProtocolType.Tcp);
            oriMessage.setPort(((InetSocketAddress)clientAddress).getPort());
            oriMessage.setAddress(((InetSocketAddress)clientAddress).getAddress().getHostAddress());
            list.add(oriMessage);
            return this.loopCheckData(clientAddress, byteBuf, list);
        }
    }
}
