package io.github.coyamo.kaop;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.coyamo.kaop.annotation.AopJoinPoint;

/**
 * @author cxc
 * @date 2022/11/9 17:13
 * @version 1.0
 */
public class Pointcut {
    final Object owner;
    private final Map<String, Method> proceedMap;
    final Map<String, List<Class<? extends Aspect>>> pluginMap;

    final Map<String, AbsJoinPoint> cache = new HashMap<>();

    public Pointcut(@NonNull Object owner, @NonNull Map<String, Method> proceedMap, @NonNull Map<String, List<Class<? extends Aspect>>> pluginMap) {
        this.owner = owner;
        this.proceedMap = proceedMap;
        this.pluginMap = pluginMap;
    }



    @Nullable
    public JoinPoint joinPoint(String point) {
        AbsJoinPoint joinPoint = cache.get(point);
        if(joinPoint instanceof JoinPoint){
            return (JoinPoint)joinPoint;
        }
        Method method = proceedMap.get(point);
        if (method != null) {
            List<Class<? extends Aspect>> list = pluginMap.get(point);
            joinPoint = new JoinPoint(owner, method, list == null ? Collections.emptyList() : list);
            cache.put(point, joinPoint);
            return (JoinPoint)joinPoint;
        }
        return null;
    }
}
