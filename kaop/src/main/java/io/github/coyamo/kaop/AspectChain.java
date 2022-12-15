package io.github.coyamo.kaop;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class AspectChain {
    private final List<Aspect> aspects = new ArrayList<>();

    public void addAspect(Aspect aspect){
        aspects.add(aspect);
    }

    public Object advice(AbsJoinPoint point){
        AbsJoinPoint localPoint = point;
        for (Aspect plugin : aspects) {
            Object result = plugin.advice(localPoint);
            localPoint = new ProxyJoinPoint(localPoint, result);
        }
        return localPoint.proceed();
    }
}
