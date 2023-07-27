package site.bleem.boot.socket.data;

import io.netty.channel.Channel;
import site.bleem.boot.socket.enums.ConnectProtocolType;

import java.time.Duration;
import java.time.LocalDateTime;

public class ConnectionEntity {
    private ConnectProtocolType protocolType;
    private LocalDateTime lastHeartBeatTime;
    private LocalDateTime firstHeartBeatTime;
    private int heartBeatCount;
    private String equIdentity;
    private String IP;
    private int port;
    private int trendHeartBeatTimeoutSecond;
    private Channel channel;
    private final int heartBeatAnalysisCount = 4;
    private boolean reportLinkState;
    private boolean analysisHeartBeat = false;

    public ConnectionEntity(Channel channel, String equIdentity, ConnectProtocolType protocolType, String clientAddress, int clientPort) {
        this.channel = channel;
        this.lastHeartBeatTime = LocalDateTime.now();
        this.heartBeatCount = 0;
        this.trendHeartBeatTimeoutSecond = 30;
        this.equIdentity = equIdentity;
        this.reportLinkState = false;
        this.IP = clientAddress;
        this.port = clientPort;
        this.protocolType = protocolType;
    }

    public void setLastHeartBeatTime(LocalDateTime lastHeartBeatTime) {
        if (this.heartBeatCount == 0) {
            this.firstHeartBeatTime = lastHeartBeatTime;
        }

        if (this.heartBeatCount < 4) {
            ++this.heartBeatCount;
        }

        this.lastHeartBeatTime = lastHeartBeatTime;
        this.analysisTrendHeartBeatTimeoutSecond();
    }

    public void initAnalysis() {
        this.analysisHeartBeat = false;
        this.heartBeatCount = 0;
        this.trendHeartBeatTimeoutSecond = 30;
    }

    private void analysisTrendHeartBeatTimeoutSecond() {
        if (this.heartBeatCount == 4 && !this.analysisHeartBeat) {
            Duration timespan = Duration.between(this.firstHeartBeatTime, this.lastHeartBeatTime);
            int analysisResult = (int)(timespan.getSeconds() / (long)(this.heartBeatCount - 1));
            if (analysisResult > 30) {
                this.trendHeartBeatTimeoutSecond = analysisResult;
            }

            this.analysisHeartBeat = true;
        }

    }

    public String getClientAddress() {
        return String.format("%s:%s", this.IP, this.port);
    }

    public ConnectProtocolType getProtocolType() {
        return this.protocolType;
    }

    public LocalDateTime getLastHeartBeatTime() {
        return this.lastHeartBeatTime;
    }

    public LocalDateTime getFirstHeartBeatTime() {
        return this.firstHeartBeatTime;
    }

    public int getHeartBeatCount() {
        return this.heartBeatCount;
    }

    public String getEquIdentity() {
        return this.equIdentity;
    }

    public String getIP() {
        return this.IP;
    }

    public int getPort() {
        return this.port;
    }

    public int getTrendHeartBeatTimeoutSecond() {
        return this.trendHeartBeatTimeoutSecond;
    }

    public Channel getChannel() {
        return this.channel;
    }

    public int getHeartBeatAnalysisCount() {
        this.getClass();
        return 4;
    }

    public boolean isReportLinkState() {
        return this.reportLinkState;
    }

    public boolean isAnalysisHeartBeat() {
        return this.analysisHeartBeat;
    }

    public void setProtocolType(ConnectProtocolType protocolType) {
        this.protocolType = protocolType;
    }

    public void setFirstHeartBeatTime(LocalDateTime firstHeartBeatTime) {
        this.firstHeartBeatTime = firstHeartBeatTime;
    }

    public void setHeartBeatCount(int heartBeatCount) {
        this.heartBeatCount = heartBeatCount;
    }

    public void setEquIdentity(String equIdentity) {
        this.equIdentity = equIdentity;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setTrendHeartBeatTimeoutSecond(int trendHeartBeatTimeoutSecond) {
        this.trendHeartBeatTimeoutSecond = trendHeartBeatTimeoutSecond;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public void setReportLinkState(boolean reportLinkState) {
        this.reportLinkState = reportLinkState;
    }

    public void setAnalysisHeartBeat(boolean analysisHeartBeat) {
        this.analysisHeartBeat = analysisHeartBeat;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ConnectionEntity)) {
            return false;
        } else {
            ConnectionEntity other = (ConnectionEntity)o;
            if (!other.canEqual(this)) {
                return false;
            } else if (this.getHeartBeatCount() != other.getHeartBeatCount()) {
                return false;
            } else if (this.getPort() != other.getPort()) {
                return false;
            } else if (this.getTrendHeartBeatTimeoutSecond() != other.getTrendHeartBeatTimeoutSecond()) {
                return false;
            } else if (this.getHeartBeatAnalysisCount() != other.getHeartBeatAnalysisCount()) {
                return false;
            } else if (this.isReportLinkState() != other.isReportLinkState()) {
                return false;
            } else if (this.isAnalysisHeartBeat() != other.isAnalysisHeartBeat()) {
                return false;
            } else {
                Object this$protocolType = this.getProtocolType();
                Object other$protocolType = other.getProtocolType();
                if (this$protocolType == null) {
                    if (other$protocolType != null) {
                        return false;
                    }
                } else if (!this$protocolType.equals(other$protocolType)) {
                    return false;
                }

                label91: {
                    Object this$lastHeartBeatTime = this.getLastHeartBeatTime();
                    Object other$lastHeartBeatTime = other.getLastHeartBeatTime();
                    if (this$lastHeartBeatTime == null) {
                        if (other$lastHeartBeatTime == null) {
                            break label91;
                        }
                    } else if (this$lastHeartBeatTime.equals(other$lastHeartBeatTime)) {
                        break label91;
                    }

                    return false;
                }

                Object this$firstHeartBeatTime = this.getFirstHeartBeatTime();
                Object other$firstHeartBeatTime = other.getFirstHeartBeatTime();
                if (this$firstHeartBeatTime == null) {
                    if (other$firstHeartBeatTime != null) {
                        return false;
                    }
                } else if (!this$firstHeartBeatTime.equals(other$firstHeartBeatTime)) {
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

                label70: {
                    Object this$IP = this.getIP();
                    Object other$IP = other.getIP();
                    if (this$IP == null) {
                        if (other$IP == null) {
                            break label70;
                        }
                    } else if (this$IP.equals(other$IP)) {
                        break label70;
                    }

                    return false;
                }

                Object this$channel = this.getChannel();
                Object other$channel = other.getChannel();
                if (this$channel == null) {
                    if (other$channel != null) {
                        return false;
                    }
                } else if (!this$channel.equals(other$channel)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof ConnectionEntity;
    }

}
