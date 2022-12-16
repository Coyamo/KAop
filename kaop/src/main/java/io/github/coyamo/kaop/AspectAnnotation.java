package io.github.coyamo.kaop;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.coyamo.kaop.Aspect;

/**
 * 用于标记指定的注解为切面注解
 *
 * @author Coyamo
 * @version 1.0
 * @date 2022/12/17 00:47
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface AspectAnnotation {
    /**
     * 切面的实现类
     */
    Class<? extends Aspect> plugin();
}
