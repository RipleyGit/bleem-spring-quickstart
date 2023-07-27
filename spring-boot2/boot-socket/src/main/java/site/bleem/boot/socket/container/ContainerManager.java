package site.bleem.boot.socket.container;

import io.netty.channel.Channel;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class ContainerManager {
    private NettyRpcClientContainer nettyRpcClientContainer;
    private SendSeqContainer sendSeqContainer;
    private Channel UDPBroadcastChannel;

    public ContainerManager() {
    }

    @PostConstruct
    public void init() {
        this.nettyRpcClientContainer = new NettyRpcClientContainer();
        this.sendSeqContainer = new SendSeqContainer();
    }

    public NettyRpcClientContainer getNettyRpcClientContainer() {
        return this.nettyRpcClientContainer;
    }

    public SendSeqContainer getSendSeqContainer() {
        return this.sendSeqContainer;
    }

    public Channel getUDPBroadcastChannel() {
        return this.UDPBroadcastChannel;
    }

    public void setUDPBroadcastChannel(Channel UDPBroadcastChannel) {
        this.UDPBroadcastChannel = UDPBroadcastChannel;
    }
}
