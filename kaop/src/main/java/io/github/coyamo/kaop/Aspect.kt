package io.github.coyamo.kaop


/**
 * 切面
 * @author Coyamo
 * @date 2022/12/17 00:47
 * @version 1.0
 */
abstract class Aspect {
    /**
     * 开始处理
     */
    fun advice(point: AbsJoinPoint): Any? {
        before(point)
        val result = around(point)
        after(point)
        return result
    }

    open fun before(point: AbsJoinPoint) {}

    open fun around(point: AbsJoinPoint): Any? {
        return point.proceed()
    }

    open fun after(point: AbsJoinPoint) {}
}