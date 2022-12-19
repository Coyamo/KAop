package io.github.coyamo.kaopdemo

import android.app.Application
import io.github.coyamo.kxxpermissions.KXXPermission

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        KXXPermission.init(this)
    }
}