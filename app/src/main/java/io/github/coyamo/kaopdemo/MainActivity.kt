package io.github.coyamo.kaopdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import io.github.coyamo.kaop.KAop
import io.github.coyamo.kaopdemo.aspect.auth.AuthAspect
import io.github.coyamo.kaopdemo.aspect.auth.NeedToken
import io.github.coyamo.kaopdemo.aspect.timecost.TimeCost

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
        findViewById<Button>(R.id.action).setOnClickListener {
            val result = click2()
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.logout).setOnClickListener {
            AuthAspect.token = null
        }
        findViewById<Button>(R.id.admin_login).setOnClickListener {
            AuthAspect.token = "admin"
        }
        findViewById<Button>(R.id.action2).setOnClickListener {
            val result = clickStatic()
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        }
        findViewById<Button>(R.id.action3).setOnClickListener {
            val result = clickStatic2()
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
        }
    }

    //用法1 手动创建代理的方法 在代理的方法通过point名字手动调用被代理的方法
    @TimeCost
    private fun login():String = pointcut{
        try {
            Thread.sleep(1000)
        }catch (_:Exception){}
        AuthAspect.token = "user"
        return@pointcut "done!"
    }


    //用法2 函数具体实现使用`KAop.inject(this)`{ ... } 包装一次
    @TimeCost
    @NeedToken
    private fun click2():String = pointcut{
        return@pointcut "操作成功"
    }

    companion object{

        private val pointcut = KAop(this)

        @NeedToken
        private fun clickStatic():String = pointcut{
            return@pointcut "操作成功"
        }

        @NeedToken
        @JvmStatic
        private fun clickStatic2():String = pointcut{
            return@pointcut "操作成功"
        }
    }
}