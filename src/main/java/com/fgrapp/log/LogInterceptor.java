package com.fgrapp.log;

import cn.hutool.core.lang.UUID;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author fgr
 * @date 2022-11-21 19:31
 **/
@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        MDC.put(Constants.START_TIME, String.valueOf(System.currentTimeMillis()));
        //添加MDC值
        String traceId = UUID.fastUUID().toString(true);
        MDC.put(Constants.LOG_MDC_ID, traceId);
        // 将traceId加入响应header中
        response.addHeader(Constants.LOG_MDC_ID, traceId);
        //打印接口请求信息
        String method = request.getMethod();
        String uri = request.getRequestURI();
        log.info("[请求接口] : {} : {}", method, uri);
        return true;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) throws Exception {
        String startStr = MDC.get(Constants.START_TIME);
        log.info("请求耗时:{}",System.currentTimeMillis() - Long.parseLong(startStr));
        //打印请求结果
        //删除MDC值
        MDC.remove(Constants.LOG_MDC_ID);
        MDC.remove(Constants.START_TIME);
    }
}
