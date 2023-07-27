package site.bleem.boot.socket.enums;

public enum CmdResultCode {
    Successed(0, "成功"),
    Fail_EquIdentity_unMatch(-1, "设备标识匹配失败"),
    Fail_ParamError(-2, "参数不正确"),
    Fail_Socket_Inactive(-3, "链接不可用"),
    Fail_Timeout(-4, "超时"),
    Fail_SendError(-5, "发送失败"),
    Fail_Response_Fail(-6, "设备响应返回失败"),
    Fail_Routing_Fail(-7, "路由寻址失败");

    private int code;
    private String desc;

    private CmdResultCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
