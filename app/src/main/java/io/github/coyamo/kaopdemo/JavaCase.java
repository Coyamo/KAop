package io.github.coyamo.kaopdemo;

import io.github.coyamo.kaop.KAop;
import io.github.coyamo.kaop.MethodGetter;
import io.github.coyamo.kaop.Pointcut;
import io.github.coyamo.kaopdemo.aspect.auth.NeedToken;

public class JavaCase {
    private final Pointcut pointcut = KAop.inject(this);

    @NeedToken
    public String test(){
        return new MethodGetter<String>(pointcut, ()-> "操作成功"){}.proxy();
    }

    private static final Pointcut pointcutStatic = KAop.inject(JavaCase.class);

    @NeedToken
    public static String testStatic(){
        return new MethodGetter<String>(pointcutStatic, ()-> "操作成功"){}.proxy();
    }
}
