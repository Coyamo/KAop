package io.github.coyamo.kaopdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import io.github.coyamo.kaop.KAop
import io.github.coyamo.kaop.annotation.AopJoinPoint
import io.github.coyamo.kaop.invoke
import io.github.coyamo.kaopdemo.aspect.auth.AuthAspect
import io.github.coyamo.kaopdemo.aspect.auth.NeedToken
import io.github.coyamo.kaopdemo.aspect.timecost.TimeCost

class MainActivity : AppCompatActivity() {

    //要实现aop 必须要初始化
    private val pointcut = KAop.inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        findViewById<Button>(R.id.click).setOnClickListener {
            val result = click()
            AuthAspect.token = "user"
            Log.d("MainActivity", "click1 returns: $result")
        }
        findViewById<Button>(R.id.click2).setOnClickListener {
            val result = click2()
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
            Log.d("MainActivity", "click2 returns: $result")
        }
        findViewById<Button>(R.id.click3).setOnClickListener {
            AuthAspect.token = "admin"
        }
        findViewById<Button>(R.id.click4).setOnClickListener {
            AuthAspect.token = null
        }
    }

    //用法1 手动创建代理的方法 在代理的方法通过point名字手动调用被代理的方法
    fun click():String{
        return pointcut.joinPoint("click")!!.join() as String
    }

    @TimeCost
    @AopJoinPoint("click")
    fun clickReal():String{
        Log.d("MainActivity", "clickReal: Hello")
        try {
            Thread.sleep(1000)
        }catch (_:Exception){}
        Log.d("MainActivity", "clickReal: World")
        return "done!"
    }


    //用法2 函数具体实现使用`KAop.inject(this)`{ ... } 包装一次
    @TimeCost
    @NeedToken
    @AopJoinPoint
    fun click2():String = pointcut{
        Log.d("MainActivity", "click2: Do something...")
        try {
            Thread.sleep(500)
        }catch (_:Exception){}
        Log.d("MainActivity", "click2: Do something done")
        return@pointcut "操作成功"
    }
}