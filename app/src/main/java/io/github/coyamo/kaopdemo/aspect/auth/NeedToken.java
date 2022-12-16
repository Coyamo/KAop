package io.github.coyamo.kaopdemo.aspect.auth;

import io.github.coyamo.kaop.AspectAnnotation;

@AspectAnnotation(plugin = AuthAspect.class)
public @interface NeedToken{
}
