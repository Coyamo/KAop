package io.github.coyamo.kaop

/**
 * 获取并且代理当前方法的中间接口
 * @author Coyamo
 * @date 2022/12/17 00:47
 * @version 1.0
 */
open class MethodGetter<T>(private val pointcut:Pointcut, private val block: PointScope.() -> T) {
    fun proxy(): T{
        return pointcut.pointcut(this, block)
    }
}