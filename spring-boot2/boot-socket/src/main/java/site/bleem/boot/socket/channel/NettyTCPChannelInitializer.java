package site.bleem.boot.socket.channel;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import site.bleem.boot.socket.dispatch.AbstractDataDispatch;

public class NettyTCPChannelInitializer extends ChannelInitializer<SocketChannel> {
    private AbstractDataDispatch dataDispatch;

    public NettyTCPChannelInitializer(AbstractDataDispatch defaultDispatch) {
        this.dataDispatch = defaultDispatch;
    }

    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new ChannelHandler[]{new TCPMessageDecoder()});
        socketChannel.pipeline().addLast(new ChannelHandler[]{new ProtocolChannelInboundHandler(this.dataDispatch)});
        socketChannel.pipeline().addLast(new ChannelHandler[]{new TCPServerExceptionHandler()});
    }
}