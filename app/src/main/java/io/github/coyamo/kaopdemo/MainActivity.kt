package io.github.coyamo.kaopdemo

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import io.github.coyamo.kaop.KAop
import io.github.coyamo.kaopdemo.aspect.auth.AuthAspect
import io.github.coyamo.kaopdemo.aspect.timecost.TimeCost
import io.github.coyamo.kxxpermissions.KXXPermission
import io.github.coyamo.kxxpermissions.permissionDenied
import io.github.coyamo.kxxpermissions.permissionGranted

class MainActivity : AppCompatActivity() {

    //要实现aop 必须要初始化
    private val pointcut = KAop.inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<Button>(R.id.user_login).setOnClickListener {
            val result = login()
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.admin_login).setOnClickListener {
            AuthAspect.token = "admin"
        }
        findViewById<Button>(R.id.logout).setOnClickListener {
            AuthAspect.token = null
        }


        val ktClassCase = KtClassCase()
        findViewById<Button>(R.id.action).setOnClickListener {
            val result = ktClassCase.test()
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.action2).setOnClickListener {
            val result = KtObjectCase.testStatic()
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.action3).setOnClickListener {
            val result = KtObjectCase.testJvmStatic()
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        }

        val javaCase = JavaCase()
        findViewById<Button>(R.id.action4).setOnClickListener {
            val result = javaCase.test()
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.action5).setOnClickListener {
            val result = JavaCase.testStatic()
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        }

        findViewById<Button>(R.id.action6).setOnClickListener {
            useCamera()
        }
    }

    @TimeCost
    private fun login() = pointcut{
        try {
            Thread.sleep(1000)
        }catch (_:Exception){}
        AuthAspect.token = "user"
        return@pointcut "done!"
    }

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

}