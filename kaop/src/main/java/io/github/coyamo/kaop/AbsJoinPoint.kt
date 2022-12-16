package io.github.coyamo.kaop

import java.lang.reflect.Method

/**
 * 一个JoinPoint
 * @author Coyamo
 * @date 2022/12/17 00:47
 * @version 1.0
 */
abstract class AbsJoinPoint {
    /**
     * 执行
     */
    abstract fun proceed(): Any?

    /**
     * 所属对象
     */
    abstract val owner: Any

    /**
     * 方法本体
     */
    abstract val method: Method

    /**
     * 方法的参数
     */
    abstract val params: Array<Any?>
}