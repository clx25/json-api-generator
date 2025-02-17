package com.clxin.generator;

import com.clxin.controller.ApiViewController;
import com.clxin.filter.ApiBeanFilter;
import com.clxin.filter.ParamFilterHandler;
import com.clxin.model.ApiInfo;
import com.clxin.provider.DocProvider;
import lombok.AllArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;


@AllArgsConstructor
public class DefaultApiJsonGenerator implements ApiJsonGenerator {

    private final ApiViewController<Map<String, List<ApiInfo>>> apiViewController;

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    private final DocProvider docProvider;

    private final List<ApiBeanFilter> apiBeanFilters;

    private final ParamFilterHandler paramFilterHandler;

    private final DefaultResultGenerator defaultResultGenerator;


    @Override
    public void generate() {
        docProvider.initCommentData();
        //获取springmvc解析好的请求映射
        Map<RequestMappingInfo, HandlerMethod> handlerMethods =
                requestMappingHandlerMapping.getHandlerMethods();

        Map<String, List<ApiInfo>> apiInfoMap = new TreeMap<>();

        //遍历映射
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {

            ApiInfo apiInfo = new ApiInfo();

            HandlerMethod handlerMethod = entry.getValue();

            //请求的方法的类
            Class<?> beanType = handlerMethod.getBeanType();
            //排除一些controller
            if (exclude(beanType)) {
                continue;
            }
            //获取返回对象解析值
            Object returnInfo = defaultResultGenerator.getReturnInfo(handlerMethod);
            apiInfo.setResult(returnInfo);
            //获取类注释
            String classComment = docProvider.getClassComment(beanType);
            if (ObjectUtils.isEmpty(classComment)) {
                continue;
            }


            RequestMappingInfo requestMappingInfo = entry.getKey();

            //获取请求url
            PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();

            if (patternsCondition != null) {
                //获取请求方式，默认一个方法只有一种请求方式与一个url，所以在获取到第一个个之后就break
                RequestMethodsRequestCondition methodsCondition = requestMappingInfo.getMethodsCondition();
                for (RequestMethod method : methodsCondition.getMethods()) {
                    apiInfo.setHttpMethod(method.name());
                    break;
                }
                //获取匹配规则
                Set<String> patterns = patternsCondition.getPatterns();
                for (String pattern : patterns) {
                    apiInfo.setUrl(pattern);
                    break;
                }
            }


            //获取,设置方法注释
            String methodComment = docProvider.getMethodComment(handlerMethod.getMethod());
            apiInfo.setApiDes(methodComment);

            //设置这个方法的入参信息
            for (MethodParameter methodParameter : handlerMethod.getMethodParameters()) {

                Class<?> parameterType = methodParameter.getParameterType();
                //过滤掉一些类
                if (!paramFilterHandler.accept(parameterType)) {
                    continue;
                }
                AbstractMap.SimpleEntry<String, String> paramInfo = docProvider.getParamComment(methodParameter.getMethod(), methodParameter.getParameterIndex());
                if (ObjectUtils.isEmpty(paramInfo)) {
                    continue;
                }
                if (parameterType.isPrimitive() || parameterType == String.class || parameterType.isArray() || parameterType.isAssignableFrom(Map.class)) {
                    apiInfo.getAccept().put(paramInfo.getKey(), paramInfo.getValue());
                } else {
                    Object o = null;
                    try {
                        o = parameterType.getConstructor().newInstance();
                    } catch (Exception e) {
                        //
                    }
                    apiInfo.getAccept().put(paramInfo.getKey(), o);
                }
            }


            //同一个类会有多个方法，所以先获取
            List<ApiInfo> oldApiInfos = apiInfoMap.get(classComment);
            if (!ObjectUtils.isEmpty(oldApiInfos)) {
                oldApiInfos.add(apiInfo);
            } else {
                List<ApiInfo> apiInfos = new ArrayList<>();
                apiInfos.add(apiInfo);
                apiInfoMap.put(classComment, apiInfos);
            }
        }
        apiViewController.setApiViewInfo(apiInfoMap);
    }


    private boolean exclude(Class<?> apiBeanClass) {
        for (ApiBeanFilter apiBeanFilter : apiBeanFilters) {
            if (apiBeanFilter.exclude(apiBeanClass)) {
                return true;
            }
        }
        return false;
    }
}
