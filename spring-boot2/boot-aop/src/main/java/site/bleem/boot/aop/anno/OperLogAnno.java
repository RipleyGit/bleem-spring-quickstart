package site.bleem.boot.aop.anno;

import site.bleem.boot.aop.enums.OperLogEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OperLogAnno {
    OperLogEnum value();

}
