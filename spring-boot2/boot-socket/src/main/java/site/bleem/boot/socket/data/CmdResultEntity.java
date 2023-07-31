package site.bleem.boot.socket.data;

public class CmdResultEntity {
    private String uuid;
    private int resultCode;
    private String sendTime;
    private String responseTime;

    public CmdResultEntity() {
    }

    public String getUuid() {
        return this.uuid;
    }

    public int getResultCode() {
        return this.resultCode;
    }

    public String getSendTime() {
        return this.sendTime;
    }

    public String getResponseTime() {
        return this.responseTime;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CmdResultEntity)) {
            return false;
        } else {
            CmdResultEntity other = (CmdResultEntity)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getResultCode() != other.getResultCode()) {
                return false;
            } else {
                label49: {
                    Object this$uuid = this.getUuid();
                    Object other$uuid = other.getUuid();
                    if (this$uuid == null) {
                        if (other$uuid == null) {
                            break label49;
                        }
                    } else if (this$uuid.equals(other$uuid)) {
                        break label49;
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

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof CmdResultEntity;
    }

}
