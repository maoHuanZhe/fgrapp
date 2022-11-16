package com.fgrapp.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author fgr
 * @date 2022-11-05 18:51
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@TableName("func_class")
@NoArgsConstructor
@AllArgsConstructor
public class FuncClassDo extends BaseDo {
    /**
     * 分类名称
     */
    @NotBlank(message = "分类名称不能为空")
    private String name;
}
