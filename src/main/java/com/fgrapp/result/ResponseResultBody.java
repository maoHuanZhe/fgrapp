package com.fgrapp.result;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;
/**
 * @author fgr
 * @date 2022-11-01 20:39
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ResponseBody
@RestController
public @interface ResponseResultBody {

}

