package com.fgrapp.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * SysUser
 * 用户对象 sys_user
 *
 * @author fan guang rui
 * @date 2021年06月09日 18:59
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("sys_user")
public class SysUserDo extends BaseDo {

    private static final long serialVersionUID = 103773122639812862L;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 微信编号
     */
    private String openId;

    /**
     * 用户性别
     */
    private String sex;

    /**
     * 用户头像
     */
    private String avatar;
    private Boolean isAdmin;
}
