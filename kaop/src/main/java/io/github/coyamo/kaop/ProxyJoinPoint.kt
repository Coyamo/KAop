package io.github.coyamo.kaop

import java.lang.reflect.Method

/**
 * @author Coyamo
 * @date 2022/12/17 00:47
 * @version 1.0
 */
internal class ProxyJoinPoint(private val joinPoint: AbsJoinPoint, private val result: Any?) :
    AbsJoinPoint() {
    override fun proceed(): Any? {
        return result
    }

    override val owner: Any
        get() = joinPoint.owner
    override val method: Method
        get() = joinPoint.method
    override val params: Array<Any?>
        get() = joinPoint.params
}