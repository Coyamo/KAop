package io.github.coyamo.kaopdemo.aspect.timecost;

import android.util.Log;

import androidx.annotation.NonNull;

import io.github.coyamo.kaop.AbsJoinPoint;
import io.github.coyamo.kaop.Aspect;
import io.github.coyamo.kaop.JoinPoint;

public class TimeCostAspect extends Aspect {
    private long time = 0;

    @Override
    public void before(@NonNull AbsJoinPoint point) {
        time = System.currentTimeMillis();
        Log.d("TimeCost", "TimeCostAspect before");
    }

    @Override
    public void after(@NonNull AbsJoinPoint point) {
        Log.d("TimeCost", (System.currentTimeMillis() - time) + "ms");
        Log.d("TimeCost", "TimeCostAspect after");
    }
}
