package site.bleem.boot.socket.enums;

public enum DispatchTaskType {
    Up(0, "上行"),
    Down(1, "下行");

    private int code;
    private String desp;

    private DispatchTaskType(int code, String desp) {
        this.desp = desp;
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
