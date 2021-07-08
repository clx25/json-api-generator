package com.clxin.scanner;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * 未使用
 */
public class ApiScanner extends ClassPathBeanDefinitionScanner {


    /**
     * 扫描的注解
     */
    private Class<? extends Annotation> annotationClass;
    /**
     * 扫描的包
     */
    private List<String> packages;

    private ResourcePatternResolver resourcePatternResolver;

    private final String DEFAULT_RESOURCE_PATTERN = "**/*.class";


    public ApiScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> annotation, List<String> packages) {
        super(registry, false);
        this.annotationClass = annotation;
        this.addIncludeFilter(new AnnotationTypeFilter(this.annotationClass));
        this.packages = packages;
    }

    public void scan() {
        for (String pkg : packages) {
            String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                    resolveBasePackage(pkg) + '/' + DEFAULT_RESOURCE_PATTERN;
            //通过资源加载器获取包路径下的class文件
            try {
                Resource[] resources = getResourcePatternResolver().getResources(packageSearchPath);
                for (Resource resource : resources) {
                    if (resource.isReadable()) {
                        MetadataReader metadataReader = getMetadataReaderFactory().getMetadataReader(resource);
                        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
                        boolean b = annotationMetadata.hasAnnotation(annotationClass.getName());
                        if (!b) {
                            continue;
                        }

                        AnnotationAttributes attributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(RequestMapping.class.getName(), false));
                        String[] path = null;
                        if (attributes != null) {
                            path = attributes.getStringArray("value");
                        }


                        Set<MethodMetadata> annotatedMethods = annotationMetadata.getAnnotatedMethods(RequestMapping.class.getName());
                        for (MethodMetadata annotatedMethod : annotatedMethods) {
                            AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(annotatedMethod.getAnnotationAttributes(RequestMapping.class.getName(), false));
                            String methodPath[]=null;
                            if (annotationAttributes != null) {
                                methodPath=annotationAttributes.getStringArray("value");
                            }

                        }
                    }
                }
            } catch (IOException e) {
                //
            }
        }

    }


    private ResourcePatternResolver getResourcePatternResolver() {
        if (this.resourcePatternResolver == null) {
            this.resourcePatternResolver = new PathMatchingResourcePatternResolver();
        }
        return this.resourcePatternResolver;
    }


}
