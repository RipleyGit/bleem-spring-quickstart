package site.bleem.feign.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

public class JacksonObjectMapper extends ObjectMapper {
    private static final long serialVersionUID = 4288193147502386170L;
    private static final Locale CHINA;

    public JacksonObjectMapper() {
        super.setLocale(CHINA);
        super.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        super.setTimeZone(TimeZone.getTimeZone(ZoneId.systemDefault()));
        super.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA));
        super.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
        super.configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature(), true);
        super.findAndRegisterModules();
        super.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        super.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        super.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        super.getDeserializationConfig().withoutFeatures(new DeserializationFeature[]{DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES});
        super.registerModule(new JavaTimeModule());
        super.findAndRegisterModules();
    }

    public ObjectMapper copy() {
        return super.copy();
    }

    static {
        CHINA = Locale.CHINA;
    }
}