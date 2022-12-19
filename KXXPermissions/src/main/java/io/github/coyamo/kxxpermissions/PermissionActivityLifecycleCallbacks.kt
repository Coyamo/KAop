package io.github.coyamo.kxxpermissions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.os.Bundle

@SuppressLint("StaticFieldLeak")
object PermissionActivityLifecycleCallbacks: Application.ActivityLifecycleCallbacks {
    var currentActivity:Activity? = null
        private set
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        currentActivity = activity
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        currentActivity = null
    }
}