package com.example.weddingplanner.repository;

import com.example.weddingplanner.model.Task;
import com.example.weddingplanner.model.TaskPriority;
import com.example.weddingplanner.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByWeddingId(Long weddingId);

    List<Task> findByWeddingIdAndStatus(Long weddingId, TaskStatus status);

    List<Task> findByWeddingIdAndPriority(Long weddingId, TaskPriority priority);
}
