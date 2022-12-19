# KAop
A weird runtime AOP framework.
一个奇怪的运行时AOP框架。

## 原理
用一个接口包装真正的方法的逻辑，再通过创建匿名类获取当前执行的方法`Method`并代理执行前面包装的逻辑。在中间通过自定义的切面控制方法该如何调用。缺点是对每一个使用KAop的方法创建两个类，牺牲空间和性能换取AOP。

## 使用KAop封装框架
1.[DEMO]权限请求[KXXPermissions](https://github.com/Coyamo/KAop/blob/main/KXXPermissions/README.md),基于[XXPermissions](https://github.com/getActivity/XXPermissions)

## 编写AOP切面
1.继承`Aspect`，如`TimeCostAspect`，实现了对方法执行时间的计算。
```java
public class TimeCostAspect extends Aspect {
    private long time = 0;

    @Override
    public void before(@NonNull AbsJoinPoint point) {
        time = System.currentTimeMillis();
    }

    @Override
    public void after(@NonNull AbsJoinPoint point) {
        Log.d("TimeCost", (System.currentTimeMillis() - time) + "ms");
    }
}
```
2.
**对于Kotlin调用：** 定义一个注解，使用`@AspectAnnotation`注解该注解，并且指定`plugin`使用`TimeCostAspect`。
其中`int order() default 0;`代表切面的执行顺序，越小越先执行。`order`为一个约定的名称，可以不定义该参数。默认为`Int.MAX_VALUE`。  
**对于Java调用：** 必须要标记注解`@Retention(RetentionPolicy.RUNTIME)`，kotlin默认是RUNTIME。
```java
@AspectAnnotation(plugin = TimeCostAspect.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeCost {
    int order() default 0;
}
```
## 使用AOP切面
**Kotlin**  
在需要切面的类初始化,在需要切面的函数标记定义的注解，函数用`pointcut{...}`包裹一层，其中`pointcut`是初始化返回的对象，这里override了他的invoke操作。
```kotlin
class KtClassCase {
    private val pointcut = KAop(this)

    @NeedToken
    fun test():String =  pointcut{
        return@pointcut "操作成功"
    }

}

object KtObjectCase {

    private val pointcut = KAop(this)

    @NeedToken
    fun testStatic():String =  pointcut{
        return@pointcut "操作成功"
    }
    
    @NeedToken
    @JvmStatic
    fun testJvmStatic():String = pointcut{
        return@pointcut "操作成功"
    }
}
```
**Java**  
在Java中使用略显繁琐，这里只是做了个对Java的兼容，建议使用Kotlin。  
注意，创建MethodGetter对象时后面的`{}`
正确：new MethodGetter<...>(..., ...){}.proxy();  
错误：new MethodGetter<...>(..., ...).proxy();
```kotlin
public class JavaCase {
    private final Pointcut pointcut = KAop.inject(this);

    @NeedToken
    public String test(){
        return new MethodGetter<String>(pointcut, scope-> "操作成功"){}.proxy();
    }

    private static final Pointcut pointcutStatic = KAop.inject(JavaCase.class);

    @NeedToken
    public static String testStatic(){
        return new MethodGetter<String>(pointcutStatic, scope-> "操作成功"){}.proxy();
    }
}
```
