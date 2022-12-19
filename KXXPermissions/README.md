# 基于KAop封装XXPermissions使用注解的安卓权限请求框架
&emsp;&emsp;基于之前写的一个框架[KAop](https://github.com/Coyamo/KAop)（详见[《某种使用Kotlin的Android运行时AOP》](https://blog.csdn.net/qq_37348364/article/details/128356014)），封装了[XXPermissions](https://github.com/getActivity/XXPermissions)权限请求框架，实现了一个简易的通过注解请求权限的框架~
&emsp;&emsp;注：KAop目前还只是个玩具...所以大概率可能应该多多少少是有一点问题的。不要把这个demo放正式开发里。

## 编写AOP切面
1.继承`Aspect`，拿到注解里面的权限参数使用XXPermissions请求，请求回调里面从方法`scope`获取执行真正的逻辑。
```kotlin
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
```
2.定义用来请求权限的注解，`init`是用来初始化辅助权限请求的。需要在调用的app的Application里面初始化。
```kotlin
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
```
3.以及对方法Scope的扩展,在这里把真正的逻辑放`interface` 用map存起来了~
```kotlin
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
```
## 使用
在需要请求权限的函数上使用注解，记得初始化`KXXPermission.init(this)`
```kotlin
@KXXPermission([Manifest.permission.CAMERA])
private fun useCamera() = pointcut{
	permissionDenied { permissions,nerver->
		Toast.makeText(this@MainActivity, if(nerver) "永久拒绝授权" else "拒绝授权", Toast.LENGTH_SHORT).show()
	}

	permissionGranted{permissions,all->
		Toast.makeText(this@MainActivity, "已经授权", Toast.LENGTH_SHORT).show()
	}
	return@pointcut
}
```
## 总结
&emsp;&emsp;Emm...好像也没有比直接用原框架省事多少...这里是把成功和失败的代码都整一起去了，也可以设计成单独的两个方法，成功继续执行失败执行另一个方法。