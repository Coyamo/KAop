package io.github.coyamo.kaop

import android.text.TextUtils
import android.util.Log
import io.github.coyamo.kaop.annotation.AopJoinPoint
import java.lang.reflect.Method

inline operator fun<reified T> Pointcut.invoke(noinline block:()-> T):T{
    //这里必须要新建一个对象才可以获取到enclosingMethod
     return object : MethodGetter<T> {
         override fun proxy(): T {
             val method = javaClass.enclosingMethod
             return if(method == null){
                 Log.w("KAop", "Obtaining enclosing method failed!")
                 block()
             } else {
                 joinPoint(method, block)?.join() as T
             }
         }
     }.proxy()
}
fun Pointcut.joinPoint(method: Method, block:()-> Any?): JoinPoint? {
    var joinPoint = cache[method.toString()]
    //默认名字是否有缓存
    if (joinPoint is JoinPointWrapper && joinPoint.method == method) {
        return joinPoint
    }
    val proceedAnnotation = method.getAnnotation(AopJoinPoint::class.java)
    if (proceedAnnotation != null) {
        var point: String = proceedAnnotation.value
        if (TextUtils.isEmpty(point)) {
            point = method.toString()
        }
        joinPoint = JoinPointWrapper(owner, method, block,pluginMap[point] ?: emptyList())
        cache[point] = joinPoint
        return joinPoint
    }
    return null
}

class JoinPointWrapper(owner:Any, method:Method, val block:()-> Any?, plugins:List<Class<out Aspect>>) : JoinPoint(owner, method, plugins) {
    override fun proceed(): Any? {
        return block()
    }
}

interface MethodGetter<T>{
    fun proxy():T
}