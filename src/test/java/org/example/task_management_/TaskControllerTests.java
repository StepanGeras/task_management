package org.example.task_management_;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.task_management_.controller.TaskController;
import org.example.task_management_.entity.Task;
import org.example.task_management_.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TaskControllerTests {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
    }

    @Test
    public void testCreateTask() throws Exception {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Description");
        task.setStatus("pending");
        task.setPriority("high");
        task.setAssignee("assignee@example.com");

        when(taskService.createTask(task)).thenReturn(task);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    public void testUpdateTask_Success() throws Exception {
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Existing Task");
        existingTask.setDescription("Existing Description");
        existingTask.setStatus("pending");
        existingTask.setPriority("medium");
        existingTask.setAssignee("assignee@example.com");

        Task updatedTaskDetails = new Task();
        updatedTaskDetails.setTitle("Updated Task");
        updatedTaskDetails.setDescription("Updated Description");
        updatedTaskDetails.setStatus("in_progress");
        updatedTaskDetails.setPriority("high");

        when(taskService.findTaskByIdAndUser(1L)).thenReturn(Optional.of(existingTask));
        when(taskService.updateTask(1L, updatedTaskDetails)).thenReturn(updatedTaskDetails);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedTaskDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.status").value("in_progress"));
    }

    @Test
    public void testUpdateStatus_Success() throws Exception {
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Existing Task");
        existingTask.setDescription("Existing Description");
        existingTask.setStatus("pending");
        existingTask.setPriority("medium");
        existingTask.setAssignee("assignee@example.com");

        Task updatedTaskDetails = new Task();
        updatedTaskDetails.setStatus("in_progress");

        when(taskService.findTaskByIdAndUser(1L)).thenReturn(Optional.empty());
        when(taskService.findTaskByIdAndAssignee(1L)).thenReturn(Optional.of(existingTask));
        when(taskService.updateStatus(1L, updatedTaskDetails)).thenReturn(existingTask);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedTaskDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("pending"));
    }

    @Test
    public void testUpdateTask_Failure() throws Exception {
        Task updatedTaskDetails = new Task();
        updatedTaskDetails.setTitle("Updated Task");

        when(taskService.findTaskByIdAndUser(1L)).thenReturn(Optional.empty());
        when(taskService.findTaskByIdAndAssignee(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedTaskDetails)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Error! Wrong id"));
    }

    @Test
    public void testDeleteTask_Success() throws Exception {
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Task to Delete");

        when(taskService.findTaskByIdAndUser(1L)).thenReturn(Optional.of(existingTask));
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @Test
    public void testDeleteTask_Failure() throws Exception {
        when(taskService.findTaskByIdAndUser(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error! Wrong id"));
    }
}

