package com.htrucci.apiversion.handler;

import com.htrucci.apiversion.config.ApiVersion;
import com.htrucci.apiversion.vo.Version;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CmtApiVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    public CmtApiVersionRequestMappingHandlerMapping() {
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
        if(info == null) return null;
        ApiVersion classAnnotation = AnnotationUtils.findAnnotation(method.getDeclaringClass(), ApiVersion.class);
        ApiVersion methodAnnotation = AnnotationUtils.findAnnotation(method, ApiVersion.class);
        RequestMapping classRequestMapping = AnnotationUtils.findAnnotation(method.getDeclaringClass(), RequestMapping.class);
        RequestMapping methodRequestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
        List<RequestMapping> requestMappingList = new ArrayList<>();
        RequestCondition<?> methodCondition = getCustomMethodCondition(method);

        if(classAnnotation != null && methodAnnotation == null){
            // 클래스 Version어노테이션만 있는 경우
            if(classRequestMapping != null){
                requestMappingList.add(classRequestMapping);
            }
            info = createClassApiVersionInfo(info, classAnnotation, methodCondition, requestMappingList );
        }else if(classAnnotation == null && methodAnnotation != null){
            // 메소드 Version어노테이션만 있는 경우
            if(classRequestMapping != null){
                requestMappingList.add(classRequestMapping);
            }
            if(methodRequestMapping != null){
                requestMappingList.add(methodRequestMapping);
            }
            info = createMethodApiVersionInfo(methodAnnotation, methodCondition, requestMappingList );
        }else if(classAnnotation != null && methodAnnotation != null){
            // 둘다 있는 경우, 메소드 어노테이션이 우선권이 있다.
            if(classRequestMapping != null){
                requestMappingList.add(classRequestMapping);
            }
            info = createApiVersionInfo(info, methodAnnotation, methodCondition, requestMappingList);
        }

        return info;
    }
    @Override
    protected RequestMappingInfo createRequestMappingInfo(RequestMapping requestMapping, RequestCondition<?> customCondition) {
        String[] patterns = requestMapping.value();
        for(int i=0; i<patterns.length; i++){
            if(patterns[i].contains("{version}")){
                int replaceEndIdx = StringUtils.indexOf(patterns[i],"{version}");
                patterns[i] = StringUtils.substring(patterns[i], replaceEndIdx+9, patterns[i].length());
            }
        }
        return new RequestMappingInfo(
                new PatternsRequestCondition(patterns, getUrlPathHelper(), getPathMatcher(), useSuffixPatternMatch(), useTrailingSlashMatch(), getFileExtensions()),
                new RequestMethodsRequestCondition(),
                new ParamsRequestCondition(),
                new HeadersRequestCondition(),
                new ConsumesRequestCondition(),
                new ProducesRequestCondition(),
                customCondition);
    }
    private RequestMappingInfo createClassApiVersionInfo(RequestMappingInfo info, ApiVersion apiVersion, RequestCondition<?> methodCondition, List<RequestMapping> requestMappingList) {
        Version[] versions = apiVersion.version();
        for (int i = 0; i < versions.length; i++) {
            info = createClassVersionRequestMappingInfo(versions[i].getVersion(), methodCondition, requestMappingList).combine(info);
        }
        return info;
    }

    private RequestMappingInfo createApiVersionInfo(RequestMappingInfo info, ApiVersion apiVersion, RequestCondition<?> methodCondition, List<RequestMapping> requestMappingList) {
        info = createVersionRequestMappingInfo(apiVersion, methodCondition, requestMappingList).combine(info);

        return info;
    }

    private RequestMappingInfo createClassVersionRequestMappingInfo(String apiVersion, RequestCondition<?> customCondition, List<RequestMapping> requestMappingList) {

        String url = "";
        for(int i=0; i<requestMappingList.size(); i++){
            String[] mappingValue = requestMappingList.get(i).value();
            System.out.println(mappingValue[0]);
            url += mappingValue[0];
        }
        String[] patterns = {new String(StringUtils.replaceOnce(url, "{version}", apiVersion))};
        return new RequestMappingInfo(
                new PatternsRequestCondition(patterns, getUrlPathHelper(), getPathMatcher(), useSuffixPatternMatch(), useTrailingSlashMatch(), getFileExtensions()),
                new RequestMethodsRequestCondition(),
                new ParamsRequestCondition(),
                new HeadersRequestCondition(),
                new ConsumesRequestCondition(),
                new ProducesRequestCondition(),
                customCondition);
    }

    private RequestMappingInfo createMethodApiVersionInfo(ApiVersion apiVersion, RequestCondition<?> methodCondition, List<RequestMapping> requestMappingList) {
        Version[] versions = apiVersion.version();
        String[] url = new String[versions.length];
        for(int i=0; i<versions.length; i++){
            for(int j=0; j<requestMappingList.size(); j++){
                String[] mappingValue = requestMappingList.get(j).value();
                System.out.println(mappingValue[0]);
                url[i] += mappingValue[0];
            }
            url[i] = StringUtils.replaceOnce(url[i], "{version}", versions[i].getVersion());
            url[i] = StringUtils.replace(url[i], "{version}", "");
        }

        String[] patterns = url;
        return new RequestMappingInfo(
                new PatternsRequestCondition(patterns, getUrlPathHelper(), getPathMatcher(), useSuffixPatternMatch(), useTrailingSlashMatch(), getFileExtensions()),
                new RequestMethodsRequestCondition(),
                new ParamsRequestCondition(),
                new HeadersRequestCondition(),
                new ConsumesRequestCondition(),
                new ProducesRequestCondition(),
                methodCondition);

    }

    private RequestMappingInfo createVersionRequestMappingInfo(ApiVersion apiVersion, RequestCondition<?> customCondition, List<RequestMapping> requestMappingList) {
        String[] url = new String[apiVersion.version().length];
        for(int i=0; i<apiVersion.version().length; i++){
            url[i] = "";
            for(int j=0; j<requestMappingList.size(); j++){
                String[] mappingValue = requestMappingList.get(j).value();
                url[i] += mappingValue[0];
            }
            url[i] = StringUtils.replaceOnce(url[i], "{version}", apiVersion.version()[i].getVersion());
            url[i] = StringUtils.replace(url[i], "{version}", "");
        }
        String[] patterns = url;
        return new RequestMappingInfo(
                new PatternsRequestCondition(patterns, getUrlPathHelper(), getPathMatcher(), useSuffixPatternMatch(), useTrailingSlashMatch(), getFileExtensions()),
                new RequestMethodsRequestCondition(),
                new ParamsRequestCondition(),
                new HeadersRequestCondition(),
                new ConsumesRequestCondition(),
                new ProducesRequestCondition(),
                customCondition);
    }

}
