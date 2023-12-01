package site.bleem.boot.web.validate;

import cn.hutool.core.util.IdcardUtil;
import org.springframework.util.ObjectUtils;
import site.bleem.boot.web.util.ResetMessageUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 身份证号码，格式校验器
 */
public class IdCardValidator implements ConstraintValidator<IdCard, String> {

    @Override
    public void initialize(IdCard constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (ObjectUtils.isEmpty(value)) {
            return true;
        }

        if (value.contains(" ")) {
            ResetMessageUtil.reset(context, "身份证号码，格式错误：不能包含空格");
            return false;
        }

        return IdcardUtil.isValidCard(value);
    }

}

