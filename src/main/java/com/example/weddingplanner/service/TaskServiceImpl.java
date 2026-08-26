package com.example.weddingplanner.service;

import com.example.weddingplanner.model.Task;
import com.example.weddingplanner.model.TaskPriority;
import com.example.weddingplanner.model.TaskStatus;
import com.example.weddingplanner.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService{

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getTasksForWedding(Long weddingId){
        return taskRepository.findByWeddingId(weddingId);
    }

    @Override
    public List<Task> getTasksByStatus(Long weddingId, TaskStatus status){
        return taskRepository.findByWeddingIdAndStatus(weddingId, status);
    }

    @Override
    public List<Task> getTasksByPriority(Long weddingId, TaskPriority priority){
        return taskRepository.findByWeddingIdAndPriority(weddingId, priority);
    }

    @Override
    public Optional<Task> getTaskById(Long id){
        return taskRepository.findById(id);
    }

    @Override
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }

    @Override
    public long countTasks(Long weddingId){
        return taskRepository.findByWeddingId(weddingId).size();
    }

    @Override
    public long countCompletedTasks(Long weddingId){
        return taskRepository.findByWeddingId(weddingId)
                .stream()
                .filter(task -> task.getStatus() == TaskStatus.COMPLETED)
                .count();
    }
}
