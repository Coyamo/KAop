package io.github.coyamo.kxxpermissions

import android.app.Application
import io.github.coyamo.kaop.AspectAnnotation

@AspectAnnotation(plugin = PermissionAspect::class)
@Retention
@Target(AnnotationTarget.FUNCTION)
annotation class KXXPermission(val value:Array<String>, val order: Int = 1){
    companion object{
        fun init(app:Application){
            app.registerActivityLifecycleCallbacks(PermissionActivityLifecycleCallbacks)
        }
    }
}
