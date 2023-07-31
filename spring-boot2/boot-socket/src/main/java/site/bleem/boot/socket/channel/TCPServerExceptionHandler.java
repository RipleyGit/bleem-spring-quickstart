package site.bleem.boot.socket.channel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TCPServerExceptionHandler extends ChannelInboundHandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(TCPServerExceptionHandler.class);

    public TCPServerExceptionHandler() {
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        try {
            ctx.close();
            if (cause instanceof IOException) {
                return;
            }

            log.error("exceptionCaught:" + ctx.channel().remoteAddress(), cause);
        } catch (Exception var4) {
            log.info("exceptionCaught:{}", var4);
        }

    }
}
