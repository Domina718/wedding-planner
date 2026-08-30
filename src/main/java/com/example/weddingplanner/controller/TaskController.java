package com.example.weddingplanner.controller;

import com.example.weddingplanner.model.Task;
import com.example.weddingplanner.model.TaskPriority;
import com.example.weddingplanner.model.TaskStatus;
import com.example.weddingplanner.model.Wedding;
import com.example.weddingplanner.service.TaskService;
import com.example.weddingplanner.service.WeddingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TaskController {

    private final TaskService taskService;
    private final WeddingService weddingService;

    public TaskController(TaskService taskService, WeddingService weddingService){
        this.taskService = taskService;
        this.weddingService = weddingService;
    }

    @GetMapping("/tasks")
    public String showTasks(
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            Model model){

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding has not been created yet."));

        List<Task> tasks;

        if (status != null){
            tasks = taskService.getTasksByStatus(wedding.getId(), status);
        }
        else if(priority != null){
            tasks = taskService.getTasksByPriority(wedding.getId(), priority);
        }
        else{
            tasks = taskService.getTasksForWedding(wedding.getId());
        }

        model.addAttribute("tasks", tasks);
        model.addAttribute("task", new Task());
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("priorities", TaskPriority.values());

        return "tasks";
    }

    @PostMapping("/tasks/save")
    public String saveTask(@ModelAttribute Task task){

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding has not been created yet."));

        task.setWedding(wedding);
        taskService.saveTask(task);

        return "redirect:/tasks";
    }

    @GetMapping("/tasks/edit/{id}")
    public String editTask(@PathVariable Long id, Model model){

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding not found."));
        Task task = taskService.getTaskById(id, wedding.getId())
                .orElseThrow(()-> new IllegalArgumentException("Task not found."));

        model.addAttribute("task", task);
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("priorities", TaskPriority.values());

        return "task-edit";
    }

    @PostMapping("/tasks/update")
    public String updateTask(@ModelAttribute Task task) {
        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding not found."));

        Task existingTask = taskService.getTaskById(task.getId(), wedding.getId())
                        .orElseThrow(()->new IllegalArgumentException("Task not found."));

        existingTask.setTitle(task.getTitle());
        existingTask.setDescription(task.getDescription());
        existingTask.setDueDate(task.getDueDate());
        existingTask.setPriority(task.getPriority());
        existingTask.setStatus(task.getStatus());

        taskService.saveTask(existingTask);

        return "redirect:/tasks";
    }

    @PostMapping("/tasks/delete/{id}")
    public String deleteTask(@PathVariable Long id){

        Wedding wedding = weddingService.getWedding()
                        .orElseThrow(()-> new IllegalStateException("Wedding not found."));

        taskService.deleteTask(id, wedding.getId());

        return "redirect:/tasks";
    }
}
