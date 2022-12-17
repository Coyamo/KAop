package io.github.coyamo.kaopdemo.aspect.auth;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.github.coyamo.kaop.AspectAnnotation;

@AspectAnnotation(plugin = AuthAspect.class)
//如果是kotlin使用 默认为RUNTIME
//Java调用则需要标记为RUNTIME
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedToken{
}
