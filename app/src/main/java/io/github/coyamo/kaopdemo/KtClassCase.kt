package io.github.coyamo.kaopdemo

import io.github.coyamo.kaop.KAop
import io.github.coyamo.kaopdemo.aspect.auth.NeedToken

class KtClassCase {
    private val pointcut = KAop(this)

    @NeedToken
    fun test():String =  pointcut{
        return@pointcut "操作成功"
    }

}