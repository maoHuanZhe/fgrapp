package com.fgrapp.domain.vo;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author fgr
 * @date 2022-11-05 06:54
 **/
@Data
public class RegisterVo {
    @NotBlank(message = "参数不能为空")
    private String phoneOrEmail;
    @Size(min = 6, max = 6, message = "验证码长度应为六位")
    private String code;
}
