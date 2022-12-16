package io.github.coyamo.kaop

import android.util.Log
import android.util.Pair
import io.github.coyamo.kaop.KAop.DEFAULT_ORDER
import io.github.coyamo.kaop.KAop.ORDER_FILED_NAME
import java.lang.reflect.Method

/**
 * @author Coyamo
 * @date 2022/12/17 00:47
 * @version 1.0
 */
class Pointcut(
    private val owner: Any
) {
    private val pluginMap = mutableMapOf<String, List<Class<out Aspect?>>>()
    private val cache: MutableMap<String, AbsJoinPoint> = HashMap()

    inline operator fun <reified T> invoke(noinline block: () -> T): T {
        //这里必须要新建一个对象才可以获取到enclosingMethod
        return object : MethodGetter<T> {
            override fun proxy(): T {
                val method = javaClass.enclosingMethod
                return if (method == null) {
                    Log.w("KAop", "Obtaining enclosing method failed!")
                    block()
                } else {
                    val joinPoint = joinPoint(method, block)
                    return if (joinPoint == null) {
                        block()
                    } else {
                        joinPoint.join() as T
                    }
                }
            }
        }.proxy()
    }


    fun joinPoint(method: Method, block: () -> Any?): JoinPoint? {
        val point = method.toString()
        var joinPoint = cache[point]
        //默认名字是否有缓存
        if (joinPoint is JoinPoint && joinPoint.method == method) {
            Log.d("KAop", "Load ${method.name} from cache")
            return joinPoint
        }
        val annotations = method.annotations

        var isPointcut = false
        val tmpPlugins: MutableList<Pair<Int, Class<out Aspect?>>> = ArrayList()
        for (annotation in annotations) {
            val annotationClass: Class<*> = annotation.annotationClass.java
            val aopPluginAnnotation = annotationClass.getAnnotation(
                AspectAnnotation::class.java
            )
            if (aopPluginAnnotation != null) {
                isPointcut = true
                var order = DEFAULT_ORDER
                try {
                    val orderMethod = annotation.javaClass.getDeclaredMethod(ORDER_FILED_NAME)
                    val returnType = orderMethod.returnType
                    if (returnType == Int::class.java || returnType == Int::class.javaPrimitiveType) {
                        order = orderMethod.invoke(annotation) as Int
                    }
                } catch (ignore: Exception) {
                }
                tmpPlugins.add(Pair(order, aopPluginAnnotation.plugin.java))
            }
        }

        if (!isPointcut) {
            Log.d("KAop", "${method.name}:Redundant operations")
            return null
        }

        //排序
        tmpPlugins.sortWith { o1, o2 ->
            Integer.compare(
                o1.first,
                o2.first
            )
        }
        val plugins: MutableList<Class<out Aspect?>> = ArrayList()
        for (tmpPlugin in tmpPlugins) {
            plugins.add(tmpPlugin.second)
        }
        pluginMap[point] = plugins
        joinPoint = JoinPoint(owner, method, block, plugins)
        cache[point] = joinPoint
        return joinPoint
    }
}