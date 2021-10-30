package com.clxin.generator;

import com.clxin.config.ResultExample;
import com.clxin.provider.DocProvider;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.method.HandlerMethod;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

import javax.annotation.Resource;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class DefaultResultGenerator {

    @Autowired(required = false)
    private ResultExample resultExample;




    public Object getReturnInfo(HandlerMethod handlerMethod) {
        MethodParameter methodParameter = handlerMethod.getReturnType();
        Type type = methodParameter.getGenericParameterType();

        return typeToValue(type);
//        //这个是返回类型的最外层类型
//        Class<?> returnType = methodParameter.getParameterType();
//        //获取类的泛型，这个类可能不是泛型类，所以typeParameters可能长度为0
//        TypeVariable<? extends Class<?>>[] typeParameters = returnType.getTypeParameters();
//        if (typeParameters.length > 0) {
//            //这里获取的是返回类的泛型定义类型名称，如：List<T> 这里获取的就是T
//            List<String> typeNames = Arrays.stream(typeParameters).map(Type::getTypeName).collect(Collectors.toList());
//
//            Field[] declaredFields = returnType.getDeclaredFields();
//            List<String> fieldTypeNames = Arrays.stream(declaredFields).filter(f -> f.getGenericType() instanceof TypeVariableImpl).map(f -> f.getGenericType().getTypeName()).collect(Collectors.toList());
//
//            //获取方法上定义的泛型的实际类型
//            Type[] actualTypeArguments = ((ParameterizedTypeImpl) methodParameter.getGenericParameterType()).getActualTypeArguments();
//
//            for (String typeName : typeNames) {
//                for (String fieldTypeName : fieldTypeNames) {
//                    if (typeName.equals(fieldTypeName)) {
//
//                    }
//                }
//            }
//
//        }


//        Map.Entry<Object, String> entry;
//        if (Void.class.isAssignableFrom(returnType) ||
//                void.class.isAssignableFrom(returnType)) {//void,Void
//
//            return "无返回";
//
//        } else if (CharSequence.class.isAssignableFrom(returnType) ||
//                char.class.isAssignableFrom(returnType) ||
//                Character.class.isAssignableFrom(returnType)) {//CharSequence,char,Character
//
//            return "";
//
//        } else if (ClassUtils.isPrimitiveOrWrapper(returnType)) {//基本类型和其包装类
//
//            return 0;
//
//        } else if (ResponseEntity.class.isAssignableFrom(returnType)) {//ResponseEntity
//
//            return ResponseEntity.ok(getGenericObject(methodParameter));
//
//        }
//
//        return "返回异常";
    }

    private Map.Entry<String, Object> isCustomResult(Class<?> returnType) {
        if (resultExample == null && log.isInfoEnabled()) {
            if (log.isDebugEnabled()) {
                log.debug("未配置返回对象类型");
            }
            return null;
        }
        Map<String, Object> example;

        example = resultExample.getExample();
        if (example == null || example.isEmpty()) {
            return null;
        }
        for (Map.Entry<String, Object> entry : example.entrySet()) {
            Object returnObject = entry.getValue();
            if (returnObject == null) {
                log.error("配置的示例对象不能为null", new Exception());
                return null;
            }
            return returnType.isAssignableFrom(returnObject.getClass()) ? entry : null;
        }
        return null;
    }


    private Object typeToValue(Type type) {
        if (type instanceof Class<?>) {
            return classToValue((Class<?>) type);
        } else if (type instanceof ParameterizedTypeImpl) {
            return genericClassToValue((ParameterizedTypeImpl) type);
        }

        return null;
    }


    private Object genericClassToValue(ParameterizedTypeImpl type) {
        Class<?> rawType = type.getRawType();
        if (Map.class.isAssignableFrom(rawType)) {
            Map<String, String> map = new HashMap<>();
            map.put("key1", "value1");
            map.put("key2", "value2");
            map.put("...", "...");
            return map;
        } else if (Collection.class.isAssignableFrom(rawType)) {
            return Arrays.asList("value1", "value2", "...");
        } else if (HttpEntity.class.isAssignableFrom(rawType)) {//ResponseEntity
            return ResponseEntity.ok();
        }

        return classToMap(type);

    }

    /**
     * 获取泛型定义与该泛型对应的实际类型
     *
     * @param type 泛型类型
     * @return
     */
    private Map<String, Type> determineGenericActualType(ParameterizedTypeImpl type) {
        Class<?> rawType = type.getRawType();

        //类上的泛型定义
        TypeVariable<? extends Class<?>>[] typeParameters = rawType.getTypeParameters();
        //返回类型上的泛型使用
        Type[] actualTypeArguments = type.getActualTypeArguments();
        Map<String, Type> genericMap = new HashMap<>();

        if (actualTypeArguments != null &&
                typeParameters.length != 0 &&
                (typeParameters.length == actualTypeArguments.length)) {
            for (int i = 0; i < typeParameters.length; i++) {
                genericMap.put(typeParameters[i].getTypeName(), actualTypeArguments[i]);
            }
        }
//        Map<String, Type> map = new HashMap<>();
//        //域上的泛型使用
//        for (Field field : rawType.getDeclaredFields()) {
//            Type genericType = field.getGenericType();
//            if (genericType instanceof TypeVariableImpl) {
//                String name = field.getName();
//                String typeName = genericType.getTypeName();
//                Type fieldType = genericMap.get(typeName);
//                map.put(name, fieldType);
//            }
//        }
        return genericMap;
    }


    private Object instance(Class<?> rawType, Method method) {
        try {
            return rawType.getConstructor().newInstance();
        } catch (Exception e) {
            String logMeg = "方法" + method + "创建返回对象失败";
            log.error(logMeg, e);
            return logMeg;
        }
    }

    /**
     * 类转为map
     * @param type
     * @return
     */
    private Map<String, Object> classToMap(Type type) {
        Class<?> rawType;
        if (type instanceof Class<?>) {
            rawType = (Class<?>) type;
        } else {
            rawType = ((ParameterizedTypeImpl) type).getRawType();
        }
        //获取该类包括父类和Object中的get,set方法
        PropertyDescriptor[] propertyDescriptors = new PropertyDescriptor[0];
        try {
            propertyDescriptors = Introspector.getBeanInfo(rawType).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            log.error("错误", e);
        }
        Map<String, Object> map = new HashMap<>();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String name = propertyDescriptor.getName();

            Type genericReturnType = propertyDescriptor.getReadMethod().getGenericReturnType();
            if ("java.lang.Class<?>".equals(genericReturnType.getTypeName())) {
                continue;
            }
            if (genericReturnType instanceof TypeVariableImpl) {
                if (type instanceof Class<?>) {
                    map.put(name, null);
                    continue;
                }
                Map<String, Type> actualType = determineGenericActualType((ParameterizedTypeImpl) type);
                Object o = typeToValue(actualType.get(genericReturnType.getTypeName()));
                map.put(name, o);


            } else {
                map.put(name, typeToValue(genericReturnType));
            }

        }
        return map;
    }

    /**
     * 类转为json中的value
     * @param returnType
     * @return
     */
    private Object classToValue(Class<?> returnType) {
        if ("java.lang.Class".equals(returnType.getName())) {
            return null;
        }
        if (Void.class.isAssignableFrom(returnType) ||
                void.class.isAssignableFrom(returnType)) {//void,Void
            return "无返回";
        } else if (CharSequence.class.isAssignableFrom(returnType) ||
                char.class.isAssignableFrom(returnType) ||
                Character.class.isAssignableFrom(returnType)) {//CharSequence,char,Character
            return "";
        } else if (ClassUtils.isPrimitiveOrWrapper(returnType)) {//基本类型和其包装类
            return 0;
        } else if (returnType == Object.class) {
            return null;
        } else {
            return classToMap(returnType);

        }
    }

}
