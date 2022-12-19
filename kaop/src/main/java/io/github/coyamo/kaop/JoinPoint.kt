package io.github.coyamo.kaop

import java.lang.reflect.Method

/**
 * @author Coyamo
 * @date 2022/12/17 00:47
 * @version 1.0
 */
class JoinPoint(
    override val owner: Any,
    override val method: Method,
    override var scope: PointScope?,
    var block: PointScope.() -> Any?,
    private val plugins: List<Class<out Aspect>>
) : AbsJoinPoint() {

    override lateinit var params: Array<Any?>
    private val aspectChain = AspectChain(this)
    private var isInit = false

    fun join(vararg args: Any?): Any? {
        //只初始化一次 同一个方法使用同一个对象 不同方法之间不共享
        if (!isInit) {
            params = arrayOf(*args)
            for (pluginClass in plugins) {
                val plugin = getAspectInstance(pluginClass)
                plugin?.let {
                    aspectChain.addAspect(it)
                }
            }
            isInit = true
        }
        val result = if (plugins.isEmpty()) {
            proceed()
        } else {
            aspectChain.advice()
        }
        return result
    }

    // TODO: 2022/12/15 后续添加更多策略 比如全局一个实例、单个类一个实例或指定某个实例
    private fun getAspectInstance(plugin: Class<out Aspect>): Aspect? {
        return try {
            plugin.newInstance()
        } catch (e: Exception) {
            null
        }
    }

    override fun proceed(): Any? {
        return scope?.block()
    }

}