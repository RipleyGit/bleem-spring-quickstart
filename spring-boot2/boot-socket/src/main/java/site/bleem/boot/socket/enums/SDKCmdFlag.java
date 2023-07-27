package site.bleem.boot.socket.enums;

public enum SDKCmdFlag {
    HeartBeat(0, "心跳数据"),
    Report(1, "上行数据"),
    SetResponse(2, "设置响应数据"),
    Set(3, "下行数据"),
    LocalConnect(4, "基站间交互"),
    ReadResponse(5, "读取响应");

    private int dataValue;
    private String desc;

    public int getDataValue() {
        return this.dataValue;
    }

    public String getDesc() {
        return this.desc;
    }

    private SDKCmdFlag(int value, String desc) {
        this.dataValue = value;
        this.desc = desc;
    }
}
