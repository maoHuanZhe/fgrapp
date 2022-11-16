package com.fgrapp.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @author fgr
 * @date 2022-11-12 13:00
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@TableName("func_comment")
@NoArgsConstructor
@AllArgsConstructor
public class CommentDo extends BaseDo {
    /**
     * 内容编号
     */
    private String contextId;
    /**
     * 上级评论用户编号
     */
    private Long upUserId;
    /**
     * 用户编号
     */
    private Long userId;
    /**
     * 内容
     */
    private String content;

//    @TableField(exist = false)
//    private String userName;
//    @TableField(exist = false)
//    private String upUserName;
}