package org.example.task_management_.service;

import org.example.task_management_.dto.CommentRequest;
import org.example.task_management_.entity.Comment;
import org.example.task_management_.entity.Task;
import org.example.task_management_.entity.User;
import org.example.task_management_.repo.CommentRepository;
import org.example.task_management_.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskRepository taskRepository;

    public Comment createComment(CommentRequest commentRequest, Long id) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

        Comment comment = new Comment();
        comment.setAuthor(user.getUsername());
        comment.setText(commentRequest.getText());
        comment.setTask(task);

        return commentRepository.save(comment);
    }

}