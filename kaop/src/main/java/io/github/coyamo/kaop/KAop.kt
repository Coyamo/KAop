package io.github.coyamo.kaop

/**
 * @author Coyamo
 * @date 2022/12/17 00:47
 * @version 1.0
 */
object KAop {
    const val ORDER_FILED_NAME = "order"
    const val DEFAULT_ORDER = Int.MAX_VALUE

    @JvmStatic
    fun inject(owner: Any): Pointcut {
        return Pointcut(owner)
    }

    /**
     * 和[inject]一样
     */
    @JvmStatic
    operator fun invoke(owner: Any) = Pointcut(owner)
}