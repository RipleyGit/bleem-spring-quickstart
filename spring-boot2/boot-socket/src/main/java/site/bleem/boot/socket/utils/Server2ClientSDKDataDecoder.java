package site.bleem.boot.socket.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;
import site.bleem.boot.socket.data.BaseSDKData;
import site.bleem.boot.socket.data.EscapeResultEntity;

public class Server2ClientSDKDataDecoder {
    public Server2ClientSDKDataDecoder() {
    }

    public static byte[] Decode(BaseSDKData server2ClientSDKData) {
        ByteBuf buf = Unpooled.compositeBuffer();
        buf.writeByte(server2ClientSDKData.getFrameHeader());
        int escapeIndex = buf.writerIndex();
        buf.writeByte(server2ClientSDKData.getIsEscape());
        buf.writeByte(server2ClientSDKData.getMainClass());
        buf.writeByte(server2ClientSDKData.getSubClass());
        buf.writeByte(server2ClientSDKData.getEquIdentityLen());
        buf.writeBytes(server2ClientSDKData.getEquIdentity());
        buf.writeBytes(server2ClientSDKData.getUpTime());
        buf.writeByte(server2ClientSDKData.getSeqence());
        buf.writeByte(server2ClientSDKData.getCommand());
        buf.writeByte(server2ClientSDKData.getState());
        buf.writeShortLE(server2ClientSDKData.getDateLen());
        buf.writeBytes(server2ClientSDKData.getInnerData());
        buf.writeShortLE(server2ClientSDKData.getCrc());
        buf.writeByte(server2ClientSDKData.getFrameFooter());
        EscapeResultEntity escapeResultEntity = EscapePostData(buf);
        byte[] decodeBytes = escapeResultEntity.getEscapedData();
        decodeBytes[escapeIndex] = 1;
        return decodeBytes;
    }

    private static EscapeResultEntity EscapePostData(ByteBuf oraData) {
        EscapeResultEntity escapeResultEntity = new EscapeResultEntity();
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();
        boolean isEscape = false;
        int startIndex = oraData.readerIndex();

        while(oraData.isReadable() && oraData.readerIndex() <= oraData.writerIndex()) {
            int tempIndex = oraData.readerIndex();
            byte LByte = oraData.readByte();
            if (LByte == -1 && tempIndex != 0) {
                compositeByteBuf.writeBytes(oraData, startIndex, tempIndex - startIndex);
                compositeByteBuf.writeByte(-3);
                compositeByteBuf.writeByte(2);
                startIndex = oraData.readerIndex();
                isEscape = true;
            } else if (LByte == -2 && tempIndex != oraData.writerIndex() - 1) {
                compositeByteBuf.writeBytes(oraData, startIndex, tempIndex - startIndex);
                compositeByteBuf.writeByte(-3);
                compositeByteBuf.writeByte(1);
                startIndex = oraData.readerIndex();
                isEscape = true;
            } else if (LByte == -3) {
                compositeByteBuf.writeBytes(oraData, startIndex, tempIndex - startIndex);
                compositeByteBuf.writeByte(-3);
                compositeByteBuf.writeByte(0);
                startIndex = oraData.readerIndex();
                isEscape = true;
            }
        }

        escapeResultEntity.setEscape(isEscape);
        if (startIndex != 0) {
            ByteBuf com1 = oraData.slice(startIndex, oraData.writerIndex() - startIndex);
            compositeByteBuf.writeBytes(com1);
            byte[] bytes = new byte[compositeByteBuf.readableBytes()];
            compositeByteBuf.readBytes(bytes);
            escapeResultEntity.setEscapedData(bytes);
            ReferenceCountUtil.release(com1);
        } else {
            escapeResultEntity.setEscapedData(ByteBufUtil.getBytes(oraData.readerIndex(0), 0, oraData.readableBytes()));
        }

        ReferenceCountUtil.release(compositeByteBuf);
        return escapeResultEntity;
    }
}
