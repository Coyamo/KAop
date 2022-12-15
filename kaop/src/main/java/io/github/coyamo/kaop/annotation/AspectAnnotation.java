package io.github.coyamo.kaop.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.coyamo.kaop.Aspect;

/**
 * @author cxc
 * @date 2022/11/9 17:11
 * @version 1.0
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface AspectAnnotation {
    Class<? extends Aspect> plugin();
}
