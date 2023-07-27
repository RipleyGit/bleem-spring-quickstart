package site.bleem.boot.socket.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.bleem.boot.socket.data.BaseSDKData;

public class Client2ServerSDKDataEncoder {
    private static final Logger log = LoggerFactory.getLogger(Client2ServerSDKDataEncoder.class);

    public Client2ServerSDKDataEncoder() {
    }

    public static BaseSDKData encode(ByteBuf oraData) {
        try {
            if (oraData != null && oraData.isReadable()) {
                ByteBuf targetBuf = oraData;
                if (needEscape(oraData.getByte(1))) {
                    targetBuf = escapeReceiveData(oraData);
                }

                if (!targetBuf.isReadable(5)) {
                    return null;
                } else {
                    BaseSDKData client2ServerSDKData = new BaseSDKData();
                    client2ServerSDKData.setFrameHeader(targetBuf.readByte());
                    client2ServerSDKData.setIsEscape(targetBuf.readByte());
                    client2ServerSDKData.setMainClass(targetBuf.readByte());
                    client2ServerSDKData.setSubClass(targetBuf.readByte());
                    byte equFlagLength = targetBuf.readByte();
                    if (equFlagLength != 0 && targetBuf.isReadable(equFlagLength)) {
                        byte[] equFlagBytes = new byte[equFlagLength];
                        targetBuf.readBytes(equFlagBytes);
                        client2ServerSDKData.setEquIdentityLen(equFlagLength);
                        client2ServerSDKData.setEquIdentity(equFlagBytes);
                        if (!targetBuf.isReadable(6)) {
                            return null;
                        } else {
                            byte[] timebytes = new byte[6];
                            targetBuf.readBytes(timebytes);
                            client2ServerSDKData.setUpTime(timebytes);
                            if (!targetBuf.isReadable(5)) {
                                return null;
                            } else {
                                client2ServerSDKData.setSeqence(targetBuf.readByte());
                                client2ServerSDKData.setCommand(targetBuf.readByte());
                                client2ServerSDKData.setState(targetBuf.readByte());
                                int innerDataLengthIndex = targetBuf.readerIndex();
                                short innerDataLength = targetBuf.readShortLE();
                                if (innerDataLength != 0 && targetBuf.isReadable(innerDataLength)) {
                                    client2ServerSDKData.setDateLen(innerDataLength);
                                    byte[] innerDataBytes = new byte[innerDataLength];
                                    targetBuf.readBytes(innerDataBytes);
                                    client2ServerSDKData.setInnerData(innerDataBytes);
                                    int crc16Value = CRC16Rule.calcCrc16(targetBuf, innerDataLengthIndex, innerDataLength + 2);
                                    if (!targetBuf.isReadable(3)) {
                                        return null;
                                    } else {
                                        client2ServerSDKData.setCrc(targetBuf.readShortLE());
                                        client2ServerSDKData.setFrameFooter(targetBuf.readByte());
                                        return crc16Value != client2ServerSDKData.getCrc() ? null : client2ServerSDKData;
                                    }
                                } else {
                                    return null;
                                }
                            }
                        }
                    } else {
                        return null;
                    }
                }
            } else {
                return null;
            }
        } catch (Exception var10) {
            log.error("消息解析过程异常", var10);
            return null;
        }
    }

    private static boolean needEscape(byte flag) {
        return flag == 1;
    }

    private static ByteBuf escapeReceiveData(ByteBuf oraData) {
        CompositeByteBuf compositeByteBuf = Unpooled.compositeBuffer();

        int startIndex;
        for(startIndex = oraData.readerIndex(); oraData.isReadable() && oraData.readerIndex() + 2 < oraData.writerIndex(); oraData.resetReaderIndex()) {
            int tempIndex = oraData.readerIndex();
            byte LByte = oraData.readByte();
            oraData.markReaderIndex();
            byte HByte = oraData.readByte();
            if (LByte == -3 && HByte == 2) {
                compositeByteBuf.writeBytes(oraData, startIndex, tempIndex - startIndex);
                compositeByteBuf.writeByte(-1);
                startIndex = oraData.readerIndex();
            } else if (LByte == -3 && HByte == 1) {
                compositeByteBuf.writeBytes(oraData, startIndex, tempIndex - startIndex);
                compositeByteBuf.writeByte(-2);
                startIndex = oraData.readerIndex();
            } else if (LByte == -3 && HByte == 0) {
                compositeByteBuf.writeBytes(oraData.slice(startIndex, tempIndex - startIndex));
                compositeByteBuf.writeByte(-3);
                startIndex = oraData.readerIndex();
            }
        }

        if (startIndex != oraData.writerIndex()) {
            compositeByteBuf.writeBytes(oraData.slice(startIndex, oraData.writerIndex() - startIndex));
            byte[] bytes = new byte[compositeByteBuf.readableBytes()];
            compositeByteBuf.readBytes(bytes);
            return Unpooled.wrappedBuffer(bytes);
        } else {
            return oraData;
        }
    }
}
