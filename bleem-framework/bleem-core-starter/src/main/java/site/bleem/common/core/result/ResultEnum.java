package site.bleem.common.core.result;

/**
 * 常用结果的枚举
 */
public enum ResultEnum implements IResult {
    SUCCESS(0, "接口调用成功"),
    VALIDATE_FAILED(5001, "参数校验失败"),
    COMMON_FAILED(5000, "接口调用失败"),
    FORBIDDEN(4001, "没有权限访问资源");

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}