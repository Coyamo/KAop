# KAop
A weird runtime AOP framework.
一个通过运行时创建匿名类获取方法名称实现Aop的诡异框架。

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
2.定义一个注解，使用`@AspectAnnotation`注解该注解，并且指定`plugin``使用TimeCostAspect`。
其中`int order() default 0;`代表切面的执行顺序，越先执行。`order`为一个约定的名称，可以不定义该参数。默认为`Int.MAX_VALUE`
```java
@AspectAnnotation(plugin = TimeCostAspect.class)
public @interface TimeCost {
    int order() default 0;
}
```
## 使用AOP切面
在需要切面的类初始化,在需要切面的函数标记定义的注解，函数用`pointcut{...}`包裹一层，其中`pointcut`是初始化返回的对象，这里override了他的invoke操作。
```kotlin
class MainActivity : AppCompatActivity() {

    //要实现aop 必须要初始化
    private val pointcut = KAop.inject(this)
	
	//...
	
	@TimeCost
    private fun login():String = pointcut{
        try {
            Thread.sleep(1000)
        }catch (_:Exception){}
		
        return@pointcut "done!"
    }
	
	companion object{

		//也可以这样初始化
        private val pointcut = KAop(this)

        @TimeCost
        private fun clickStatic():String = pointcut{
            return@pointcut "操作成功"
        }

        @TimeCost
        @JvmStatic
        private fun clickStatic2():String = pointcut{
            return@pointcut "操作成功"
        }
    }
}
```
