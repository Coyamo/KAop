package io.github.coyamo.kaop;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.coyamo.kaop.annotation.AspectAnnotation;
import io.github.coyamo.kaop.annotation.AopJoinPoint;

/**
 * @author: cxc
 * @date: 2022/11/9 17:30
 * @version: 1.0
 */
public class KAop {
    public static Pointcut inject(Object owner) {
        long time = System.currentTimeMillis();
        Map<String, Method> proceedMap = new HashMap<>();
        //TODO 这里要考虑多个插件直接的冲突问题 暂时不考虑
        Map<String, List<Class<? extends Aspect>>> pluginMap = new HashMap<>();


        Method[] methods = owner.getClass().getMethods();
        for (Method method : methods) {
            AopJoinPoint proceedAnnotation = method.getAnnotation(AopJoinPoint.class);
            if (proceedAnnotation != null) {
                String point = proceedAnnotation.value();
                if(TextUtils.isEmpty(point)){
                    //为了方便kt使用
                    point = method.toString();
                }
                proceedMap.put(point, method);
                Annotation[] annotations = method.getAnnotations();

                //排序
//                Arrays.sort(annotations, new Comparator<Annotation>() {
//                    @Override
//                    public int compare(Annotation o1, Annotation o2) {
//                        return 0;
//                    }
//                });


                List<Pair<Integer,Class<? extends Aspect>>> tmpPlugins = new ArrayList<>();
                for (Annotation annotation : annotations) {
                    Class<?> annotationClass = annotation.annotationType();
                    AspectAnnotation aopPluginAnnotation = annotationClass.getAnnotation(AspectAnnotation.class);
                    if (aopPluginAnnotation != null) {
                        int order = Integer.MAX_VALUE;
                        try {
                            Method orderMethod = annotation.getClass().getDeclaredMethod("order");
                            Class<?> returnType = orderMethod.getReturnType();
                            if(returnType == Integer.class || returnType == int.class){
                                Integer myOrder = (Integer) orderMethod.invoke(annotation);
                                if(myOrder != null){
                                    order = myOrder;
                                }
                            }
                        }catch (Exception ignore){}
                        tmpPlugins.add(new Pair<>(order, aopPluginAnnotation.plugin()));
                    }
                }

                //排序
                Collections.sort(tmpPlugins, (o1, o2) -> Integer.compare(o1.first, o2.first));
                List<Class<? extends Aspect>> plugins = new ArrayList<>();
                for (Pair<Integer,Class<? extends Aspect>> tmpPlugin : tmpPlugins) {
                    plugins.add(tmpPlugin.second);
                }
                pluginMap.put(point, plugins);
            }
        }
        Log.d("KAop", "Inject " + owner +" cost: " + (System.currentTimeMillis() - time) + "ms");
        return new Pointcut(owner, proceedMap, pluginMap);
    }
}
