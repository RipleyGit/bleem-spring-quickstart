package site.bleem.boot.socket.data;

import site.bleem.boot.socket.enums.CmdResultCode;

import java.time.LocalDateTime;

public class SendSessionEntity {
    private String equIdentity;
    private String sendUUID;
    private String sendDataKey;
    private int seq;
    private LocalDateTime sendTime;
    private LocalDateTime responseTime;
    private boolean hasResponse;
    private CmdResultCode responseCode;
    private int timeoutSecond;

    public SendSessionEntity() {
    }

    public String getEquIdentity() {
        return this.equIdentity;
    }

    public String getSendUUID() {
        return this.sendUUID;
    }

    public String getSendDataKey() {
        return this.sendDataKey;
    }

    public int getSeq() {
        return this.seq;
    }

    public LocalDateTime getSendTime() {
        return this.sendTime;
    }

    public LocalDateTime getResponseTime() {
        return this.responseTime;
    }

    public boolean isHasResponse() {
        return this.hasResponse;
    }

    public CmdResultCode getResponseCode() {
        return this.responseCode;
    }

    public int getTimeoutSecond() {
        return this.timeoutSecond;
    }

    public void setEquIdentity(String equIdentity) {
        this.equIdentity = equIdentity;
    }

    public void setSendUUID(String sendUUID) {
        this.sendUUID = sendUUID;
    }

    public void setSendDataKey(String sendDataKey) {
        this.sendDataKey = sendDataKey;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public void setResponseTime(LocalDateTime responseTime) {
        this.responseTime = responseTime;
    }

    public void setHasResponse(boolean hasResponse) {
        this.hasResponse = hasResponse;
    }

    public void setResponseCode(CmdResultCode responseCode) {
        this.responseCode = responseCode;
    }

    public void setTimeoutSecond(int timeoutSecond) {
        this.timeoutSecond = timeoutSecond;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof SendSessionEntity)) {
            return false;
        } else {
            SendSessionEntity other = (SendSessionEntity) o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getSeq() != other.getSeq()) {
                return false;
            } else if (this.isHasResponse() != other.isHasResponse()) {
                return false;
            } else if (this.getTimeoutSecond() != other.getTimeoutSecond()) {
                return false;
            } else {
                Object this$equIdentity = this.getEquIdentity();
                Object other$equIdentity = other.getEquIdentity();
                if (this$equIdentity == null) {
                    if (other$equIdentity != null) {
                        return false;
                    }
                } else if (!this$equIdentity.equals(other$equIdentity)) {
                    return false;
                }

                Object this$sendUUID = this.getSendUUID();
                Object other$sendUUID = other.getSendUUID();
                if (this$sendUUID == null) {
                    if (other$sendUUID != null) {
                        return false;
                    }
                } else if (!this$sendUUID.equals(other$sendUUID)) {
                    return false;
                }

                label76:
                {
                    Object this$sendDataKey = this.getSendDataKey();
                    Object other$sendDataKey = other.getSendDataKey();
                    if (this$sendDataKey == null) {
                        if (other$sendDataKey == null) {
                            break label76;
                        }
                    } else if (this$sendDataKey.equals(other$sendDataKey)) {
                        break label76;
                    }

                    return false;
                }

                Object this$sendTime = this.getSendTime();
                Object other$sendTime = other.getSendTime();
                if (this$sendTime == null) {
                    if (other$sendTime != null) {
                        return false;
                    }
                } else if (!this$sendTime.equals(other$sendTime)) {
                    return false;
                }

                Object this$responseTime = this.getResponseTime();
                Object other$responseTime = other.getResponseTime();
                if (this$responseTime == null) {
                    if (other$responseTime != null) {
                        return false;
                    }
                } else if (!this$responseTime.equals(other$responseTime)) {
                    return false;
                }

                Object this$responseCode = this.getResponseCode();
                Object other$responseCode = other.getResponseCode();
                if (this$responseCode == null) {
                    if (other$responseCode != null) {
                        return false;
                    }
                } else if (!this$responseCode.equals(other$responseCode)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof SendSessionEntity;
    }

}
