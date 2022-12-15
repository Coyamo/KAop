package io.github.coyamo.kaop;

import androidx.annotation.Nullable;

import java.lang.reflect.Method;

public abstract class AbsJoinPoint {
    @Nullable
    abstract public Object proceed();

    abstract public Object getOwner();

    abstract public Method getMethod();

    abstract public Object[] getParams();
}
