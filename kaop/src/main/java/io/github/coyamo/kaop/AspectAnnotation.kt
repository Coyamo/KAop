package io.github.coyamo.kaop

import kotlin.reflect.KClass

/**
 * 用于标记指定的注解为切面注解
 *
 * @author Coyamo
 * @version 1.0
 * @date 2022/12/17 00:47
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class AspectAnnotation(
    /**
     * 切面的实现类
     */
    val plugin: KClass<out Aspect>
)