package com.fgrapp.limit;

import java.lang.annotation.*;

/**
 * @author fgr
 * @date 2022-11-14 20:22
 **/
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Limit {

    /**
     * 缓存命名空间前缀
     */
    String prefix() default "";

}
