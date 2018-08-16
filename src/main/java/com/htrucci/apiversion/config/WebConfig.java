package com.htrucci.apiversion.config;

import com.htrucci.apiversion.handler.ApiVersionRequestMappingHandlerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 웹 설정.
 *
 * @author combine
 * @since 2013-03-14
 */
@Configuration
@ComponentScan(basePackages = {"com.htrucci.apiversion.controller"})
public class WebConfig extends WebMvcConfigurationSupport {

    /**
     * The application context.
     */
    @Autowired
    ApplicationContext applicationContext;


    @Override
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping mapping = new ApiVersionRequestMappingHandlerMapping();
        mapping.setInterceptors(getInterceptors());
        return mapping;
    }
}
