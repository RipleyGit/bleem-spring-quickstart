package site.bleem.amqp.enums;

public enum BaseInfoEnum {
    AREA("pp_area", "区域信息");

    private String code;
    private String name;

    private BaseInfoEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }
}
