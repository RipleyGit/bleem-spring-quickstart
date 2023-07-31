package site.bleem.boot.socket.data;

import io.netty.buffer.ByteBuf;
import site.bleem.boot.socket.enums.ConnectProtocolType;

public class OriMessageEntity {
    private String address;
    private int port;
    private ConnectProtocolType protocolType;
    private ByteBuf oriData;

    public OriMessageEntity() {
    }

    public String getAddress() {
        return this.address;
    }

    public int getPort() {
        return this.port;
    }

    public ConnectProtocolType getProtocolType() {
        return this.protocolType;
    }

    public ByteBuf getOriData() {
        return this.oriData;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setProtocolType(ConnectProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    public void setOriData(ByteBuf oriData) {
        this.oriData = oriData;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof OriMessageEntity)) {
            return false;
        } else {
            OriMessageEntity other = (OriMessageEntity)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getPort() != other.getPort()) {
                return false;
            } else {
                label49: {
                    Object this$address = this.getAddress();
                    Object other$address = other.getAddress();
                    if (this$address == null) {
                        if (other$address == null) {
                            break label49;
                        }
                    } else if (this$address.equals(other$address)) {
                        break label49;
                    }

                    return false;
                }

                Object this$protocolType = this.getProtocolType();
                Object other$protocolType = other.getProtocolType();
                if (this$protocolType == null) {
                    if (other$protocolType != null) {
                        return false;
                    }
                } else if (!this$protocolType.equals(other$protocolType)) {
                    return false;
                }

                Object this$oriData = this.getOriData();
                Object other$oriData = other.getOriData();
                if (this$oriData == null) {
                    if (other$oriData != null) {
                        return false;
                    }
                } else if (!this$oriData.equals(other$oriData)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof OriMessageEntity;
    }

}
