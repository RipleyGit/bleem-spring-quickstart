package site.bleem.common.security.component;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.commons.lang3.StringUtils;
import site.bleem.common.core.result.ResultEnum;
import site.bleem.common.security.exception.Auth2Exception;

import java.io.IOException;

public class Auth2ExceptionSerializer extends StdSerializer<Auth2Exception> {
    public Auth2ExceptionSerializer() {
        super(Auth2Exception.class);
    }

    public void serialize(Auth2Exception value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        String errCode = value.getErrorCode();
        System.out.println("-----------:" + errCode);
        if (StringUtils.isEmpty(errCode)) {
            errCode = String.valueOf(ResultEnum.FORBIDDEN.getCode());
        }

        gen.writeObjectField("code", errCode);
        gen.writeStringField("msg", value.getMessage());
        gen.writeStringField("data", "");
        gen.writeEndObject();
    }
}
