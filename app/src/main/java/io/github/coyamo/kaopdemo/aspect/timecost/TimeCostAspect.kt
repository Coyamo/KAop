package io.github.coyamo.kaopdemo.aspect.timecost

import android.util.Log
import io.github.coyamo.kaop.Aspect
import io.github.coyamo.kaop.AbsJoinPoint

class TimeCostAspect : Aspect() {
    private var time: Long = 0
    override fun before(point: AbsJoinPoint) {
        time = System.currentTimeMillis()
    }

    override fun after(point: AbsJoinPoint) {
        Log.d("TimeCost", (System.currentTimeMillis() - time).toString() + "ms")
    }
}