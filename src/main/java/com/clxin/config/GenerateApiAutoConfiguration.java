package com.clxin.config;

import com.clxin.controller.ApiViewController;
import com.clxin.controller.DefaultApiViewController;
import com.clxin.filter.*;
import com.clxin.generator.ApiJsonGenerator;
import com.clxin.generator.DefaultApiJsonGenerator;
import com.clxin.model.ApiInfo;
import com.clxin.provider.DefaultDocProvider;
import com.clxin.provider.DocProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;
import java.util.Map;

@Configuration
public class GenerateApiAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ApiJsonGenerator.class)
    public ApiJsonGenerator apiJsonGenerator(ApiViewController<Map<String, List<ApiInfo>>> apiViewController,
                                             RequestMappingHandlerMapping requestMappingHandlerMapping,
                                             DocProvider docProvider,
                                             List<ApiBeanFilter> apiBeanFilters) {
        return new DefaultApiJsonGenerator(apiViewController, requestMappingHandlerMapping, docProvider, apiBeanFilters);
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
    public DocFileFilter defaultDocFileFilter(){
        return new DefaultDocFileFilter();
    }
}
