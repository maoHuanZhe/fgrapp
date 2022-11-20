package com.fgrapp.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fgr
 * @date 2022-11-14 20:27
 **/
@Slf4j
public class IpUtils {
    //获取客户端IP地址
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        log.info("X-Forwarded-For:" + ip);
        return ip.split(",")[0];
    }
}
