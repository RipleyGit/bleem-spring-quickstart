package site.bleem.boot.socket.channel;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioDatagramChannel;
import site.bleem.boot.socket.dispatch.AbstractDataDispatch;

public class NettyUDPChannelInitializer extends ChannelInitializer<NioDatagramChannel> {
    private AbstractDataDispatch dataDispatch;

    public NettyUDPChannelInitializer(AbstractDataDispatch defaultDispatch) {
        this.dataDispatch = defaultDispatch;
    }

    protected void initChannel(NioDatagramChannel nioDatagramChannel) throws Exception {
        nioDatagramChannel.pipeline().addLast(new ChannelHandler[]{new UDPMessageDecoder()});
        nioDatagramChannel.pipeline().addLast(new ChannelHandler[]{new ProtocolChannelInboundHandler(this.dataDispatch)});
    }
}