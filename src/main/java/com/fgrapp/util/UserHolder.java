package com.fgrapp.util;

import com.fgrapp.domain.SysUserDo;

public class UserHolder {
    private static final ThreadLocal<SysUserDo> tl = new ThreadLocal<>();

    public static void saveUser(SysUserDo user){
        tl.set(user);
    }

    public static SysUserDo getUser(){
        return tl.get();
    }

    public static String getUserId() {
        SysUserDo user = getUser();
        if (user == null) {
            return null;
        } else {
            return user.getId().toString();
        }
    }

    public static void removeUser(){
        tl.remove();
    }
}
