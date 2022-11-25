package com.fgrapp.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author fgr
 * @date 2022-11-14 20:27
 **/
@Slf4j
public class IpUtils {
    /**
     * 获取当前的ServletRequest
     * @return
     */
    protected static HttpServletRequest servletRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }
    /**
     * 获取客户端IP地址
     * @param request
     * @return
     */
    public static String getIpAddr() {
        return servletRequest().getHeader("X-Real-IP");
    }
}
