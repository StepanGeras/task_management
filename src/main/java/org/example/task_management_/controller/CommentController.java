package org.example.task_management_.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.task_management_.dto.CommentRequest;
import org.example.task_management_.entity.Comment;
import org.example.task_management_.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Operation(summary = "Create a comment",
            description = "Creates a new comment associated with the specified ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Comment created successfully",
                    content = @Content(schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid input or request"),
            @ApiResponse(responseCode = "404",
                    description = "Resource not found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @PostMapping("/{id}")
    public ResponseEntity<Comment> createCommentById(@RequestBody CommentRequest commentRequest, @PathVariable Long id) {
        return ResponseEntity.ok(commentService.createComment(commentRequest, id));
    }

}