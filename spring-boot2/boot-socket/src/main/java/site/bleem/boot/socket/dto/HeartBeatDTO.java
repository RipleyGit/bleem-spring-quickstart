package site.bleem.boot.socket.dto;

public class HeartBeatDTO {
    private int deviceType;
    private String hid;
    private String dataTime;

    public HeartBeatDTO() {
    }

    public int getDeviceType() {
        return this.deviceType;
    }

    public String getHid() {
        return this.hid;
    }

    public String getDataTime() {
        return this.dataTime;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof HeartBeatDTO)) {
            return false;
        } else {
            HeartBeatDTO other = (HeartBeatDTO)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getDeviceType() != other.getDeviceType()) {
                return false;
            } else {
                Object this$hid = this.getHid();
                Object other$hid = other.getHid();
                if (this$hid == null) {
                    if (other$hid != null) {
                        return false;
                    }
                } else if (!this$hid.equals(other$hid)) {
                    return false;
                }

                Object this$dataTime = this.getDataTime();
                Object other$dataTime = other.getDataTime();
                if (this$dataTime == null) {
                    if (other$dataTime != null) {
                        return false;
                    }
                } else if (!this$dataTime.equals(other$dataTime)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof HeartBeatDTO;
    }
}
