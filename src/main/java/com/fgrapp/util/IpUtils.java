package com.fgrapp.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * @author fgr
 * @date 2022-11-14 20:27
 **/
@Slf4j
public class IpUtils {
    /**
     * 获取客户端IP地址
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("Frp-Real-Ip");
        log.info("Frp-Real-Ip:{}",ip);
        return ip;
    }
}
