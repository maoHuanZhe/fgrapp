package com.fgrapp.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import com.fgrapp.domain.SysUserDo;
import com.fgrapp.domain.vo.RegisterVo;
import com.fgrapp.result.ResultException;
import com.fgrapp.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author fgr
 * @date 2022-11-02 20:34
 **/
@Slf4j
@Service
public class LoginService {

    private final EmailUtil emailUtil;

    private final CacheClient cacheClient;

    private final StringRedisTemplate stringRedisTemplate;

    private final SysUserService userService;

    public LoginService(EmailUtil emailUtil, CacheClient cacheClient, StringRedisTemplate stringRedisTemplate, SysUserService userService) {
        this.emailUtil = emailUtil;
        this.cacheClient = cacheClient;
        this.stringRedisTemplate = stringRedisTemplate;
        this.userService = userService;
    }

    public void sendEmailMessage(String email) {
        //获取六位验证码
        String code = Utils.getCode();
        if (emailUtil.sendThymeleafMail(email,code)){
            //根据邮件发送短信
            log.info("给{}发送验证码:{}",email,code);
            //将验证码存入redis中 email为key code为value 过期时间为五分钟
            cacheClient.set(RedisConstants.LOGIN_CODE_KEY +email,code,RedisConstants.LOGIN_CODE_TTL,TimeUnit.MINUTES);
        } else {
            log.info("给{}发送验证码:{},失败",email,code);
            throw new ResultException("邮件发送失败");
        }
    }

    public String register(RegisterVo info) {
        String phoneOrEmail = info.getPhoneOrEmail();
        //校验验证码是否正确
        String redisCode = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + phoneOrEmail);
        if (StrUtil.isBlank(redisCode) || !info.getCode().equals(redisCode)) {
            //验证码不存在 或 不正确 返回异常
            log.error("验证码错误,info:{},redisCode:{}",info,redisCode);
            throw new ResultException("验证码错误");
        }
        if (Utils.checkEmail(phoneOrEmail)) {
            //符合邮箱格式
            return registerOfEmail(phoneOrEmail,info.getCode());
        } else if (Utils.checkMobileNumber(phoneOrEmail)) {
            //符合手机号格式
            return registerOfPhone(phoneOrEmail,info.getCode());
        } else {
            log.info("参数格式错误{}",phoneOrEmail);
            throw new ResultException("参数格式错误");
        }
    }

    public String registerOfEmail(String email, String code) {
        //获取用户信息
        SysUserDo userDo = userService.lambdaQuery().eq(SysUserDo::getEmail, email).one();
        //用户不存在则创建新用户
        if (userDo == null) {
            //创建新用户
            userDo = SysUserDo.builder()
                    .email(email)
                    .nickName("user_"+email).build();
            userService.save(userDo);
        }
        //生成token
        String token = UUID.fastUUID().toString(true);
        //将用户信息存入redis中
        Map<String, Object> objectMap = BeanUtil.beanToMap(userDo, new HashMap<>(),
                CopyOptions.create().ignoreNullValue().setFieldValueEditor((fieldName, fieldValue) -> {
                    if (fieldValue != null) {
                        fieldValue = fieldValue.toString();
                    }
                    return fieldValue;
                }));
        stringRedisTemplate.opsForHash().putAll(RedisConstants.LOGIN_USER_KEY + token,objectMap);
        stringRedisTemplate.expire(RedisConstants.LOGIN_USER_KEY + token, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);
        stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_TOKEN_KEY + userDo.getId(), token);
        //返回token
        return token;
    }
    public String registerOfPhone(String phone, String code) {
        //获取用户信息
        SysUserDo userDo = userService.lambdaQuery().eq(SysUserDo::getPhone, phone).one();
        //用户不存在则创建新用户
        if (userDo == null) {
            //创建新用户
            userDo = SysUserDo.builder()
                    .phone(phone)
                    .nickName("user_"+phone).build();
            userService.save(userDo);
        }
        //生成token
        String token = UUID.fastUUID().toString(true);
        //将用户信息存入redis中
        Map<String, Object> objectMap = BeanUtil.beanToMap(userDo, new HashMap<>(),
                CopyOptions.create().ignoreNullValue().setFieldValueEditor((fieldName, fieldValue) -> {
                    if (fieldValue != null) {
                        fieldValue = fieldValue.toString();
                    }
                    return fieldValue;
                }));
        stringRedisTemplate.opsForHash().putAll(RedisConstants.LOGIN_USER_KEY + token,objectMap);
        stringRedisTemplate.expire(RedisConstants.LOGIN_USER_KEY + token, RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);
        stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_TOKEN_KEY + userDo.getId(), token);
        //返回token
        return token;
    }

    public void logout() {
        Long userId = UserHolder.getUserId();
        String token = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_TOKEN_KEY + userId);
        stringRedisTemplate.delete(RedisConstants.LOGIN_USER_KEY + token);
        stringRedisTemplate.delete(RedisConstants.LOGIN_TOKEN_KEY + userId);
    }
    public void sendMessage(String phone) {
        //获取六位验证码
        String code = Utils.getCode();
        if (emailUtil.sendPhoneMessage(phone,code)){
            //根据手机号发送短信
            log.info("给{}发送验证码:{}",phone,code);
            //将验证码存入redis中 phone为key code为value 过期时间为五分钟
            cacheClient.set(RedisConstants.LOGIN_CODE_KEY +phone,code,RedisConstants.LOGIN_CODE_TTL,TimeUnit.MINUTES);
        } else {
            log.info("给{}发送验证码:{},失败",phone,code);
            throw new ResultException("短信发送失败");
        }
    }

    public void sendCode(String phoneOrEmail) {
        if (Utils.checkEmail(phoneOrEmail)) {
            log.info("发送邮箱验证码:{}",phoneOrEmail);
            //符合邮箱格式
            sendEmailMessage(phoneOrEmail);
        } else if (Utils.checkMobileNumber(phoneOrEmail)) {
            log.info("发送手机验证码:{}",phoneOrEmail);
            //符合手机号格式
            sendMessage(phoneOrEmail);
        } else {
            log.info("给{}发送验证码失败",phoneOrEmail);
            throw new ResultException("参数格式错误");
        }
    }
}
