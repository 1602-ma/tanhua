package com.feng.server.controller;

import com.feng.domain.vo.CommentVo;
import com.feng.domain.vo.PageResult;
import com.feng.server.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author f
 * @date 2023/5/8 22:33
 */
@RestController
@RequestMapping("/comnets")
public class CommentController {

    @Resource
    private CommentService commentService;

    /**
     * 评论列表
     * @param movementId    id
     * @param page          page
     * @param pagesize      pageSize
     * @return              res
     */
    @GetMapping
    public ResponseEntity findPage(String movementId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pagesize) {
        page = page > 0 ? page : 1;
        PageResult<CommentVo> result = commentService.findPage(movementId, page, pagesize);
        return ResponseEntity.ok(result);
    }

    /**
     * 发表评论
     * @param paramMap paramMap
     * @return         res
     */
    @PostMapping
    public ResponseEntity add(@RequestBody Map<String,String> paramMap){
        commentService.add(paramMap);
        return ResponseEntity.ok(null);
    }

    /**
     * 点赞
     * @param commentId
     * @return
     */
    @GetMapping("/{id}/like")
    public ResponseEntity<Long> like(@PathVariable("id") String commentId){
        Long total = commentService.likeComment(commentId);
        return ResponseEntity.ok(total);
    }

    /**
     * 取消点赞
     * @param commentId
     * @return
     */
    @GetMapping("/{id}/dislike")
    public ResponseEntity<Long> unlike(@PathVariable("id") String commentId){
        Long total = commentService.unlikeComment(commentId);
        return ResponseEntity.ok(total);
    }


}
