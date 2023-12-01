package site.bleem.boot.web.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 身份证号码。字符串必须是格式正确的身份证号码。
 * <p>
 * {@code null} 或 空字符串，是有效的（能够通过校验）。
 * <p>
 * 支持的类型：字符串
 *
 * @author songguanxun
 * @since 1.0
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = IdCardValidator.class)
public @interface IdCard {

    /**
     * @return the error message template
     */
    String message() default "身份证号码，格式错误";

    /**
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default {};

    /**
     * @return the payload associated to the constraint
     */
    Class<? extends Payload>[] payload() default {};

}

