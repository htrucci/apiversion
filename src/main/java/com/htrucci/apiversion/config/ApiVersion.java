package com.htrucci.apiversion.config;

import com.htrucci.apiversion.vo.Version;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiVersion {
    Version[] version();
}
