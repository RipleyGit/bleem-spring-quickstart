package site.bleem.boot.socket.enums;

public enum ConnectProtocolType {
    Tcp(0, "tcp"),
    Udp(1, "udp");

    private int code;
    private String desp;

    private ConnectProtocolType(int code, String desp) {
        this.desp = desp;
        this.code = code;
    }

    public String getDesp() {
        return this.desp;
    }

    public int getCode() {
        return this.code;
    }
}
