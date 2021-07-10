package com.clxin.config;

import com.clxin.ExecuteGeneratorListener;
import com.clxin.controller.ApiViewController;
import com.clxin.controller.DefaultApiViewController;
import com.clxin.filter.*;
import com.clxin.generator.ApiJsonGenerator;
import com.clxin.generator.DefaultApiJsonGenerator;
import com.clxin.model.ApiInfo;
import com.clxin.provider.DefaultDocProvider;
import com.clxin.provider.DocProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Configuration
@Import(DefaultApiViewController.class)
public class GenerateApiAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ApiJsonGenerator.class)
    public ApiJsonGenerator apiJsonGenerator(ApiViewController<Map<String, List<ApiInfo>>> apiViewController,
                                             RequestMappingHandlerMapping requestMappingHandlerMapping,
                                             DocProvider docProvider,
                                             List<ApiBeanFilter> apiBeanFilters,
                                             ParamFilterHandler paramFilterHandler) {
        return new DefaultApiJsonGenerator(apiViewController,
                requestMappingHandlerMapping,
                docProvider,
                apiBeanFilters,
                paramFilterHandler);
    }

    @Bean
    @ConditionalOnMissingBean(DocProvider.class)
    public DocProvider docProvider() {
        return new DefaultDocProvider();
    }

    @Bean
    @ConditionalOnMissingBean(ApiViewController.class)
    public ApiViewController<?> apiViewController() {
        return new DefaultApiViewController();
    }


    @Bean
    @ConditionalOnProperty(value = "json-api.default-filter", havingValue = "true", matchIfMissing = true)
    public ApiBeanFilter defaultApiBeanFilter() {
        return new DefaultApiBeanFilter();
    }

    @Bean
    @ConditionalOnProperty(value = "json-api.doc-filter", havingValue = "true", matchIfMissing = true)
    public DocFileFilter defaultDocFileFilter() {
        return new DefaultDocFileFilter();
    }

    @Bean
    public JsonApiProperties jsonApiProperties() {
        return new JsonApiProperties();
    }

    @Bean
    public DocFileFilterHandler docFileFilterHandler(List<DocFileFilter> docFileFilterList) {
        return new DocFileFilterHandler(docFileFilterList);
    }

    @Bean
    public ExecuteGeneratorListener executeGeneratorListener(ApiJsonGenerator apiJsonGenerator) {
        return new ExecuteGeneratorListener(apiJsonGenerator);
    }

    @Bean
    public DefaultParamFilter defaultParamFilter() {
        return new DefaultParamFilter();
    }

    @Bean
    public ParamFilterHandler paramFilterHandler(List<ParamFilter> paramFilters) {
        return new ParamFilterHandler(paramFilters);
    }

    @Bean
    @ConditionalOnMissingBean
    public ExecutorService dbExecutorService() {
        return new ThreadPoolExecutor(10, 20, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }
}
