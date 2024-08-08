package org.example.task_management_.service;

import org.example.task_management_.entity.Task;
import org.example.task_management_.entity.User;
import org.example.task_management_.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public Task createTask(Task task) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        task.setUser(user);
        return taskRepository.save(task);
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task updateTask(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setTitle(taskDetails.getTitle());
        task.setDescription(taskDetails.getDescription());
        task.setStatus(taskDetails.getStatus());
        task.setPriority(taskDetails.getPriority());
        task.setAssignee(taskDetails.getAssignee());
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id).orElseThrow();
        taskRepository.delete(task);
    }

    public Optional<Task> findTaskByIdAndAssignee(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return taskRepository.findTaskByIdAndAssignee(id, user.getUsername());
    }

    public Task updateStatus(Long id, Task taskDetails) {
        Task task = taskRepository.findById(id).orElseThrow();
        task.setStatus(taskDetails.getStatus());
        return taskRepository.save(task);
    }

    public Optional<Task> findTaskByIdAndUser(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return taskRepository.findByIdAndUser(id, user);
    }

    public List<Task> getAllTasksByUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return taskRepository.findAllByUserOrAssignee(user, user.getUsername());
    }

    public Task getTaskById(long l) {
        return taskRepository.findById(l).orElseThrow();
    }
}
