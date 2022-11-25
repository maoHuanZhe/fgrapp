package com.fgrapp.controller;

import com.fgrapp.domain.CommentDo;
import com.fgrapp.result.ResponseResultBody;
import com.fgrapp.service.CommentService;
import com.fgrapp.util.UserHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author fgr
 * @date 2022-11-12 13:08
 **/
@RequestMapping("/func/comment")
@ResponseResultBody
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping()
    public void add(@RequestBody CommentDo commentDo) {
        commentDo.setUserId(Long.getLong(UserHolder.getUserId()));
        commentService.save(commentDo);
    }
}
