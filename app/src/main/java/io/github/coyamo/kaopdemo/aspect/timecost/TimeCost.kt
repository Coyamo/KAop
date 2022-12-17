package io.github.coyamo.kaopdemo.aspect.timecost

import io.github.coyamo.kaop.AspectAnnotation

@AspectAnnotation(plugin = TimeCostAspect::class)
@Retention
annotation class TimeCost(val order: Int = 0)