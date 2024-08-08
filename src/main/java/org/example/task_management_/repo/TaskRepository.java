package org.example.task_management_.repo;

import org.example.task_management_.entity.Task;
import org.example.task_management_.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
    Optional<Task> findByIdAndUser(Long id, User user);
    Optional<Task> findTaskByIdAndAssignee(Long id, String username);

    List<Task> findAllByUserOrAssignee(User user, String username);
}
