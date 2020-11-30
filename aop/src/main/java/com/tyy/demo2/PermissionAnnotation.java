package com.tyy.demo2;

import java.lang.annotation.*;

/**
 * @author:tyy
 * @date:2020/11/30
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionAnnotation{
}
