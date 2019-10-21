package com.github.houbb.rpc.server.util;

import com.github.houbb.heaven.annotation.CommonEager;
import com.github.houbb.heaven.support.handler.IHandler;
import com.github.houbb.heaven.util.common.ArgUtil;
import com.github.houbb.heaven.util.util.ArrayUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 方法工具类
 * @author binbin.hou
 * @since 0.0.6
 */
@CommonEager
public final class MethodUtil {

    private MethodUtil(){}

    /**
     * 忽略的方法名称列表
     * （1）object 默认方法
     * （2）class 默认方法
     *
     * 可优化方案：
     * 将所有方法写死，放在列表中、
     * 缺点：占地方，无法动态更新。
     * @since 0.0.6
     */
    private static final List<String> IGNORE_METHOD_LIST;

    static {
        Set<String> methodNameSet = new HashSet<>(64);
        for(Method method : Object.class.getMethods()) {
            methodNameSet.add(method.getName());
        }
        for(Method method : Class.class.getMethods()) {
            methodNameSet.add(method.getName());
        }
        IGNORE_METHOD_LIST = new ArrayList<>(methodNameSet);
    }

    /**
     * 获取忽略的方法列表
     * @return 忽略方法名称列表
     * @since 0.0.6
     */
    public static List<String> getIgnoreMethodList() {
        return IGNORE_METHOD_LIST;
    }

    /**
     * 是否为应该忽略的方法名称
     * @param methodName 方法名称
     * @return 是否
     * @since 0.0.6
     */
    public static boolean isIgnoreMethod(final String methodName) {
        return getIgnoreMethodList().contains(methodName);
    }

    /**
     * 获取方法类型的名称
     * @param method 方法反射信息
     * @return 参数列表
     * @since 0.0.6
     */
    public static List<String> getParamTypeNames(final Method method) {
        ArgUtil.notNull(method, "method");

        Class<?>[] paramTypes = method.getParameterTypes();

        return ArrayUtil.toList(paramTypes, new IHandler<Class<?>, String>() {
            @Override
            public String handle(Class<?> aClass) {
                return aClass.getName();
            }
        });
    }

}
