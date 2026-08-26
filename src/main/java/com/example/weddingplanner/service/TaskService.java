package com.example.weddingplanner.service;

import com.example.weddingplanner.model.Task;
import com.example.weddingplanner.model.TaskPriority;
import com.example.weddingplanner.model.TaskStatus;

import java.util.List;
import java.util.Optional;

public interface TaskService {

    List<Task> getTasksForWedding(Long weddingId);

    List<Task> getTasksByStatus(Long weddingId, TaskStatus status);

    List<Task> getTasksByPriority(Long weddingId, TaskPriority priority);

    Optional<Task> getTaskById(Long id);

    Task saveTask(Task task);

    void deleteTask(Long id);

    long countTasks(Long weddingId);

    long countCompletedTasks(Long weddingId);
}
