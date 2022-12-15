package io.github.coyamo.kaopdemo.aspect.auth;

import androidx.annotation.Nullable;

import io.github.coyamo.kaop.annotation.AspectAnnotation;

@AspectAnnotation(plugin = AuthAspect.class)
public @interface NeedToken{
}
