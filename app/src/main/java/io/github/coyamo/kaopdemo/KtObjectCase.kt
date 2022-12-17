package io.github.coyamo.kaopdemo

import io.github.coyamo.kaop.KAop
import io.github.coyamo.kaopdemo.aspect.auth.NeedToken

object KtObjectCase {

    private val pointcut = KAop(this)

    @NeedToken
    fun testStatic():String =  pointcut{
        return@pointcut "操作成功"
    }

    @NeedToken
    @JvmStatic
    fun testJvmStatic():String = pointcut{
        return@pointcut "操作成功"
    }
}