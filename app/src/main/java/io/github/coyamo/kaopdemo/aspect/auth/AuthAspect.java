package io.github.coyamo.kaopdemo.aspect.auth;

import android.util.Log;

import androidx.annotation.NonNull;

import io.github.coyamo.kaop.AbsJoinPoint;
import io.github.coyamo.kaop.Aspect;

public class AuthAspect extends Aspect {
    //模拟token信息
    public static String token = null;
    @Override
    public void before(@NonNull AbsJoinPoint point) {
        Log.d("AuthAspect", "AuthAspect before");
    }

    @Override
    public Object around(@NonNull AbsJoinPoint point) {
        if(token == null){
            Log.d("AuthAspect", "用户权限不足！！");
            return "权限不足";
        } else {
            if("admin".equals(token)){
                Log.d("AuthAspect", "管理员权限");
                return super.around(point) + "@管理员";
            } else {
                Log.d("AuthAspect", "普通权限");
                return super.around(point) + "@用户";
            }
        }
    }

    @Override
    public void after(@NonNull AbsJoinPoint point) {
        Log.d("AuthAspect", "AuthAspect after");
    }
}
