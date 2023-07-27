package site.bleem.boot.socket.data;

import cn.hutool.core.codec.BCD;
import io.netty.buffer.ByteBuf;
import site.bleem.boot.socket.utils.DecodeRule;

import java.util.Arrays;

public class BaseSDKData {
    private byte frameHeader;
    private byte isEscape;
    private byte mainClass;
    private byte subClass;
    private byte equIdentityLen;
    private byte[] equIdentity;
    private byte[] upTime;
    private byte seqence;
    private byte command;
    private byte state;
    private short dateLen;
    private byte[] innerData;
    private short crc;
    private byte frameFooter;
    private ByteBuf oraData;

    public BaseSDKData() {
    }

    public String getEquIdentityStr() {
        return DecodeRule.bcdToStrLE(this.equIdentity);
    }

    public String getTimeStr() {
        return String.format("20%s", BCD.bcdToStr(this.upTime));
    }

    public byte getFrameHeader() {
        return this.frameHeader;
    }

    public void setFrameHeader(byte frameHeader) {
        this.frameHeader = frameHeader;
    }

    public byte getIsEscape() {
        return this.isEscape;
    }

    public void setIsEscape(byte isEscape) {
        this.isEscape = isEscape;
    }

    public byte getMainClass() {
        return this.mainClass;
    }

    public void setMainClass(byte mainClass) {
        this.mainClass = mainClass;
    }

    public byte getSubClass() {
        return this.subClass;
    }

    public void setSubClass(byte subClass) {
        this.subClass = subClass;
    }

    public byte getEquIdentityLen() {
        return this.equIdentityLen;
    }

    public void setEquIdentityLen(byte hidLen) {
        this.equIdentityLen = hidLen;
    }

    public void setEquIdentity(byte[] equIdentity) {
        this.equIdentity = equIdentity;
    }

    public byte[] getEquIdentity() {
        return this.equIdentity;
    }

    public byte[] getUpTime() {
        return this.upTime;
    }

    public void setUpTime(byte[] upTime) {
        this.upTime = upTime;
    }

    public byte getSeqence() {
        return this.seqence;
    }

    public void setSeqence(byte seqence) {
        this.seqence = seqence;
    }

    public byte getCommand() {
        return this.command;
    }

    public void setCommand(byte command) {
        this.command = command;
    }

    public byte getState() {
        return this.state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public short getDateLen() {
        return this.dateLen;
    }

    public void setDateLen(short dateLen) {
        this.dateLen = dateLen;
    }

    public byte[] getInnerData() {
        return this.innerData;
    }

    public void setInnerData(byte[] innerData) {
        this.innerData = innerData;
    }

    public short getCrc() {
        return this.crc;
    }

    public void setCrc(short crc) {
        this.crc = crc;
    }

    public byte getFrameFooter() {
        return this.frameFooter;
    }

    public void setFrameFooter(byte frameFooter) {
        this.frameFooter = frameFooter;
    }

    public boolean needEscape() {
        return this.isEscape == 1;
    }

    public boolean isReal() {
        return this.state >> 7 == 0;
    }

    public BaseSDKData copy() {
        BaseSDKData copyData = new BaseSDKData();
        copyData.frameHeader = this.frameHeader;
        copyData.isEscape = this.isEscape;
        copyData.mainClass = this.mainClass;
        copyData.subClass = this.subClass;
        copyData.equIdentityLen = this.equIdentityLen;
        copyData.equIdentity = Arrays.copyOf(this.equIdentity, this.equIdentityLen);
        copyData.upTime = Arrays.copyOf(this.upTime, this.upTime.length);
        copyData.seqence = this.seqence;
        copyData.command = this.command;
        copyData.state = this.state;
        copyData.dateLen = this.dateLen;
        copyData.innerData = this.innerData;
        copyData.crc = this.crc;
        copyData.frameFooter = this.frameFooter;
        return copyData;
    }

    public boolean waitack() {
        return (this.state >> 6 & 1) == 1;
    }
}
