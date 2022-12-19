package io.github.coyamo.kxxpermissions

import io.github.coyamo.kaop.PointScope

fun interface GrantedFunction{
    fun invoke(permissions: MutableList<String>, all: Boolean)
}

fun interface DeniedFunction{
    fun invoke(permissions: MutableList<String>, never: Boolean)
}

fun PointScope.permissionGranted(block:GrantedFunction){
    this["PermissionAspect@onGranted"] = block
}

fun PointScope.permissionDenied(block:DeniedFunction){
    this["PermissionAspect@onDenied"] = block
}