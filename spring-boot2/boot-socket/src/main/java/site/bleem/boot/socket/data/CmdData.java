package site.bleem.boot.socket.data;

import site.bleem.boot.socket.enums.SDKCmdFlag;

import java.util.Arrays;

public class CmdData {
    private String uuid;
    private int mainCode;
    private int subCode;
    private String equIdentity;
    private byte[] data;
    private int responseTimeoutSecond;
    private int routing;
    private int protocolType;
    private UdpConnectParam udpConnectParam;
    private SpecialParam specialParam;

    public CmdData() {
    }

    public String getUuid() {
        return this.uuid;
    }

    public int getMainCode() {
        return this.mainCode;
    }

    public int getSubCode() {
        return this.subCode;
    }

    public String getEquIdentity() {
        return this.equIdentity;
    }

    public byte[] getData() {
        return this.data;
    }

    public int getResponseTimeoutSecond() {
        return this.responseTimeoutSecond;
    }

    public int getRouting() {
        return this.routing;
    }

    public int getProtocolType() {
        return this.protocolType;
    }

    public UdpConnectParam getUdpConnectParam() {
        return this.udpConnectParam;
    }

    public SpecialParam getSpecialParam() {
        return this.specialParam;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setMainCode(int mainCode) {
        this.mainCode = mainCode;
    }

    public void setSubCode(int subCode) {
        this.subCode = subCode;
    }

    public void setEquIdentity(String equIdentity) {
        this.equIdentity = equIdentity;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setResponseTimeoutSecond(int responseTimeoutSecond) {
        this.responseTimeoutSecond = responseTimeoutSecond;
    }

    public void setRouting(int routing) {
        this.routing = routing;
    }

    public void setProtocolType(int protocolType) {
        this.protocolType = protocolType;
    }

    public void setUdpConnectParam(UdpConnectParam udpConnectParam) {
        this.udpConnectParam = udpConnectParam;
    }

    public void setSpecialParam(SpecialParam specialParam) {
        this.specialParam = specialParam;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CmdData)) {
            return false;
        } else {
            CmdData other = (CmdData)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getMainCode() != other.getMainCode()) {
                return false;
            } else if (this.getSubCode() != other.getSubCode()) {
                return false;
            } else if (this.getResponseTimeoutSecond() != other.getResponseTimeoutSecond()) {
                return false;
            } else if (this.getRouting() != other.getRouting()) {
                return false;
            } else if (this.getProtocolType() != other.getProtocolType()) {
                return false;
            } else {
                label75: {
                    Object this$uuid = this.getUuid();
                    Object other$uuid = other.getUuid();
                    if (this$uuid == null) {
                        if (other$uuid == null) {
                            break label75;
                        }
                    } else if (this$uuid.equals(other$uuid)) {
                        break label75;
                    }

                    return false;
                }

                Object this$equIdentity = this.getEquIdentity();
                Object other$equIdentity = other.getEquIdentity();
                if (this$equIdentity == null) {
                    if (other$equIdentity != null) {
                        return false;
                    }
                } else if (!this$equIdentity.equals(other$equIdentity)) {
                    return false;
                }

                if (!Arrays.equals(this.getData(), other.getData())) {
                    return false;
                } else {
                    Object this$udpConnectParam = this.getUdpConnectParam();
                    Object other$udpConnectParam = other.getUdpConnectParam();
                    if (this$udpConnectParam == null) {
                        if (other$udpConnectParam != null) {
                            return false;
                        }
                    } else if (!this$udpConnectParam.equals(other$udpConnectParam)) {
                        return false;
                    }

                    Object this$specialParam = this.getSpecialParam();
                    Object other$specialParam = other.getSpecialParam();
                    if (this$specialParam == null) {
                        if (other$specialParam != null) {
                            return false;
                        }
                    } else if (!this$specialParam.equals(other$specialParam)) {
                        return false;
                    }

                    return true;
                }
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof CmdData;
    }

    public static class SpecialParam {
        private SDKCmdFlag commandWord;

        public SpecialParam() {
        }

        public SDKCmdFlag getCommandWord() {
            return this.commandWord;
        }

        public void setCommandWord(SDKCmdFlag commandWord) {
            this.commandWord = commandWord;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof SpecialParam)) {
                return false;
            } else {
                SpecialParam other = (SpecialParam)o;
                if (!other.canEqual(this)) {
                    return false;
                } else {
                    Object this$commandWord = this.getCommandWord();
                    Object other$commandWord = other.getCommandWord();
                    if (this$commandWord == null) {
                        if (other$commandWord != null) {
                            return false;
                        }
                    } else if (!this$commandWord.equals(other$commandWord)) {
                        return false;
                    }

                    return true;
                }
            }
        }

        protected boolean canEqual(Object other) {
            return other instanceof SpecialParam;
        }
    }

    public static class UdpConnectParam {
        String ip;
        Integer port;

        public UdpConnectParam() {
        }

        public String getIp() {
            return this.ip;
        }

        public Integer getPort() {
            return this.port;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof UdpConnectParam)) {
                return false;
            } else {
                UdpConnectParam other = (UdpConnectParam)o;
                if (!other.canEqual(this)) {
                    return false;
                } else {
                    Object this$port = this.getPort();
                    Object other$port = other.getPort();
                    if (this$port == null) {
                        if (other$port != null) {
                            return false;
                        }
                    } else if (!this$port.equals(other$port)) {
                        return false;
                    }

                    Object this$ip = this.getIp();
                    Object other$ip = other.getIp();
                    if (this$ip == null) {
                        if (other$ip != null) {
                            return false;
                        }
                    } else if (!this$ip.equals(other$ip)) {
                        return false;
                    }

                    return true;
                }
            }
        }

        protected boolean canEqual(Object other) {
            return other instanceof UdpConnectParam;
        }

    }
}
