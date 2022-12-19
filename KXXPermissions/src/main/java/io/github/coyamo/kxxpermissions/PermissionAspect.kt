package io.github.coyamo.kxxpermissions

import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import io.github.coyamo.kaop.AbsJoinPoint
import io.github.coyamo.kaop.Aspect

class PermissionAspect : Aspect() {
    override fun around(point: AbsJoinPoint): Any? {
        val context = PermissionActivityLifecycleCallbacks.currentActivity
        if(context == null){
            return super.around(point)
        } else {
            val permission = point.method.getAnnotation(KXXPermission::class.java)
            if(permission == null){
                return super.around(point)
            } else {
                point.proceed()
                XXPermissions.with(context)
                    .permission(permission.value)
                    .request(object : OnPermissionCallback {
                        override fun onGranted(permissions: MutableList<String>, all: Boolean) {
                            val cbk = point.scope?.get("PermissionAspect@onGranted")
                            if(cbk is GrantedFunction){
                                cbk.invoke(permissions, all)
                            }
                        }

                        override fun onDenied(permissions: MutableList<String>, never: Boolean) {
                            val cbk = point.scope?.get("PermissionAspect@onDenied")
                            if(cbk is DeniedFunction){
                                cbk.invoke(permissions, never)
                            }
                        }
                    })
                return null
            }
        }
    }
}