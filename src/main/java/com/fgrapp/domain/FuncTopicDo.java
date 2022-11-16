package com.fgrapp.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author fgr
 * @date 2022-11-05 18:12
 **/
@Data
@Builder
@TableName("func_topic")
@NoArgsConstructor
@AllArgsConstructor
public class FuncTopicDo {
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private String id;
    private String problem;
    private String answer;
    /**
     * 摘要
     */
    private String summary;
    private Long classId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastUpdateTime;
}
