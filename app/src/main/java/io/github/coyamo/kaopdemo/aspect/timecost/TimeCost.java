package io.github.coyamo.kaopdemo.aspect.timecost;

import io.github.coyamo.kaop.AspectAnnotation;

@AspectAnnotation(plugin = TimeCostAspect.class)
public @interface TimeCost {
    int order() default 0;
}
