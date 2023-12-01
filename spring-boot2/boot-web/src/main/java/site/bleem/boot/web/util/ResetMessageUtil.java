package site.bleem.boot.web.util;

import javax.validation.ConstraintValidatorContext;

/**
 * 参数校验 - 重置提示信息工具类
 */
public class ResetMessageUtil {

    /**
     * 重置提示信息
     */
    public static void reset(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }

}


