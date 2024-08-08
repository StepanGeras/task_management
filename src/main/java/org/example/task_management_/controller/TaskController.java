package org.example.task_management_.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.example.task_management_.entity.Task;
import org.example.task_management_.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Operation(summary = "Create a new task",
            description = "Creates a new task and returns it.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Task created successfully",
                    content = @Content(schema = @Schema(implementation = Task.class))),
            @ApiResponse(responseCode = "400",
                    description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        return ResponseEntity.ok(taskService.createTask(task));
    }

    @Operation(summary = "Get all tasks",
            description = "Retrieves a list of all tasks.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Tasks retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Task.class)))),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @Operation(summary = "Get tasks by user",
            description = "Retrieves a list of tasks assigned to a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Tasks retrieved successfully",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Task.class)))),
            @ApiResponse(responseCode = "404",
                    description = "User not found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @GetMapping("/user/{username}")
    public ResponseEntity<List<Task>> getTaskByUser(@PathVariable String username) {
        return ResponseEntity.ok(taskService.getAllTasksByUser());
    }

    @Operation(summary = "Update a task",
            description = "Updates the details or status of an existing task.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Task updated successfully"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid ID or input"),
            @ApiResponse(responseCode = "404",
                    description = "Task not found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task taskDetails) {

        Optional<Task> task = taskService.findTaskByIdAndUser(id);
        Optional<Task> taskDetail = taskService.findTaskByIdAndAssignee(id);

        if (task.isPresent()) {
            return ResponseEntity.ok(taskService.updateTask(id, taskDetails));
        }

        if (taskDetail.isPresent()) {
            return ResponseEntity.ok(taskService.updateStatus(id, taskDetails));
        }

        return ResponseEntity.badRequest().body("Error! Wrong id");
    }

    @Operation(summary = "Delete a task",
            description = "Deletes a task by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Task deleted successfully"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid ID"),
            @ApiResponse(responseCode = "404",
                    description = "Task not found"),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {

        Optional<Task> task = taskService.findTaskByIdAndUser(id);

        if (task.isPresent()) {
            taskService.deleteTask(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().body("Error! Wrong id");
    }
}
