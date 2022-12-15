package io.github.coyamo.kaop;


/**
 * @author cxc
 * @date 2022/11/9 17:12
 * @version 1.0
 */
public abstract class Aspect {

    Object advice(AbsJoinPoint point){
        before(point);
        Object result = around (point);
        after(point);
        return result;
    }

    public void before(AbsJoinPoint point){}

    public Object around (AbsJoinPoint point){
        return point.proceed();
    }

    public void after(AbsJoinPoint point){}
}
