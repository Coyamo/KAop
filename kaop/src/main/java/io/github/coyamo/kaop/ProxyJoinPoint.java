package io.github.coyamo.kaop;

import androidx.annotation.Nullable;

import java.lang.reflect.Method;

class ProxyJoinPoint extends AbsJoinPoint{
    @Nullable private final Object result;
    private final AbsJoinPoint joinPoint;


    public ProxyJoinPoint(AbsJoinPoint joinPoint,@Nullable Object result){
        this.joinPoint = joinPoint;
        this.result = result;
    }
    @Override
    public Object proceed() {
        return result;
    }

    @Override
    public Object getOwner() {
        return joinPoint.getOwner();
    }

    @Override
    public Method getMethod() {
        return joinPoint.getMethod();
    }

    @Override
    public Object[] getParams() {
        return joinPoint.getParams();
    }
}
