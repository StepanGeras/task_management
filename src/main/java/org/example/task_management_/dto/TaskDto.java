package org.example.task_management_.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskDto {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    private String status;
    private String priority;
    private String author;
    private String assignee;
}
