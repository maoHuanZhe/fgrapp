package com.fgrapp.util;

/**
 * @author fgr
 */
public class RedisConstants {
    /**
     * 验证码
     */
    public static final String LOGIN_CODE_KEY = "login:code:";
    public static final Long LOGIN_CODE_TTL = 5L;
    /**
     * token
     */
    public static final String LOGIN_USER_KEY = "login:token:";
    public static final Long LOGIN_USER_TTL = 36000L;
    /**
     * user
     */
    public static final String LOGIN_TOKEN_KEY = "login:user:";

    /**
     * 互斥锁
     */
    public static final String LOCK_CACHE_KEY = "lock:cache:";
    /**
     * read
     */
    public static final String TOPIC_READ_KEY = "topic:uv:day:";
    public static final String TOPIC_PV_KEY = "topic:pv:";
    /**
     * liked
     */
    public static final String TOPIC_LIKED_KEY = "topic:liked:";
    /**
     * detail
     */
    public static final String TOPIC_DETAIL_KEY = "topic:detail:";
    public static final Long TOPIC_DETAIL_TTL = 365 * 24 * 60L;
}
