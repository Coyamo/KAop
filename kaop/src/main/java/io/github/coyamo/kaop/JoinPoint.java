package io.github.coyamo.kaop;

import androidx.annotation.Nullable;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author cxc
 * @date 2022/11/9 17:20
 * @version 1.0
 */
public class JoinPoint extends AbsJoinPoint{
    private final Object owner;
    private final Method method;
    private final List<Class<? extends Aspect>> plugins;


    private Object[] params = new Object[]{};
    private final AspectChain aspectChain = new AspectChain();

    public JoinPoint(Object owner, Method method, List<Class<? extends Aspect>> plugins) {
        this.owner = owner;
        this.method = method;
        this.plugins = plugins;
    }

    private boolean isInit = false;
    public Object join(Object... args) {
        //只初始化一次 同一个方法使用同一个对象 不同方法之间不共享
        if(!isInit){
            params = args;
            for (Class<? extends Aspect> pluginClass : plugins) {
                Aspect plugin = getAspectInstance(pluginClass);
                if (plugin != null) {
                    aspectChain.addAspect(plugin);
                }
            }
            isInit = true;
        }


        if (plugins.isEmpty()) {
            return proceed();
        } else {
            return aspectChain.advice(this);
        }
    }

    // TODO: 2022/12/15 后续添加更多策略 比如全局一个实例、单个类一个实例或指定某个实例
    @Nullable
    private Aspect getAspectInstance(Class<? extends Aspect> plugin) {
        try {
            return plugin.newInstance();
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public Object proceed() {
        Object result = null;
        try {
            result = method.invoke(owner, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    @Override
    public Object getOwner() {
        return owner;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Object[] getParams() {
        return params;
    }
}
