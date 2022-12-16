package io.github.coyamo.kaop

/**
 * 链式调用执行切面
 * @author Coyamo
 * @date 2022/12/17 00:47
 * @version 1.0
 */
internal class AspectChain {
    private val aspects: MutableList<Aspect> = ArrayList()

    fun addAspect(aspect: Aspect) {
        aspects.add(aspect)
    }

    fun advice(point: AbsJoinPoint): Any? {
        var localPoint = point
        for (plugin in aspects) {
            val result = plugin.advice(localPoint)
            localPoint = ProxyJoinPoint(localPoint, result)
        }
        return localPoint.proceed()
    }
}