package com.example.weddingplanner.controller;

import com.example.weddingplanner.dto.TaskRequest;
import com.example.weddingplanner.exception.ResourceNotFoundException;
import com.example.weddingplanner.model.Task;
import com.example.weddingplanner.model.TaskPriority;
import com.example.weddingplanner.model.TaskStatus;
import com.example.weddingplanner.model.Wedding;
import com.example.weddingplanner.service.TaskService;
import com.example.weddingplanner.service.WeddingService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
                .orElseThrow(()-> new IllegalStateException("Wedding not found."));

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
        model.addAttribute("taskRequest", new TaskRequest());
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("priorities", TaskPriority.values());

        return "tasks";
    }

    @PostMapping("/tasks/save")
    public String saveTask(@Valid @ModelAttribute("taskRequest") TaskRequest taskRequest,
                           BindingResult bindingResult,
                           Model model){

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding not found."));

        if(bindingResult.hasErrors()){
            model.addAttribute("tasks", taskService.getTasksForWedding(wedding.getId()));
            model.addAttribute("statuses", TaskStatus.values());
            model.addAttribute("priorities", TaskPriority.values());

            return "tasks";
        }

        Task task = new Task();

        task.setTitle(taskRequest.getTitle());
        task.setDescription(taskRequest.getDescription());
        task.setDueDate(taskRequest.getDueDate());
        task.setPriority(taskRequest.getPriority());
        task.setStatus(taskRequest.getStatus());

        task.setWedding(wedding);

        taskService.saveTask(task);

        return "redirect:/tasks";
    }

    @GetMapping("/tasks/edit/{id}")
    public String editTask(@PathVariable Long id, Model model){

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding not found."));

        Task task = taskService.getTaskById(id, wedding.getId())
                .orElseThrow(()-> new ResourceNotFoundException("Task not found."));

        TaskRequest taskRequest = new TaskRequest();

        taskRequest.setId(task.getId());
        taskRequest.setTitle(task.getTitle());
        taskRequest.setDescription(task.getDescription());
        taskRequest.setDueDate(task.getDueDate());
        taskRequest.setPriority(task.getPriority());
        taskRequest.setStatus(task.getStatus());

        model.addAttribute("taskRequest", taskRequest);
        model.addAttribute("statuses", TaskStatus.values());
        model.addAttribute("priorities", TaskPriority.values());

        return "task-edit";
    }

    @PostMapping("/tasks/update")
    public String updateTask(@Valid @ModelAttribute ("taskRequest") TaskRequest taskRequest,
                             BindingResult bindingResult,
                             Model model) {

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding not found."));

        if(bindingResult.hasErrors()){
            model.addAttribute("statuses", TaskStatus.values());
            model.addAttribute("priorities", TaskPriority.values());

            return "task-edit";
        }


        Task existingTask = taskService.getTaskById(taskRequest.getId(), wedding.getId())
                        .orElseThrow(()->new ResourceNotFoundException("Task not found."));

        existingTask.setTitle(taskRequest.getTitle());
        existingTask.setDescription(taskRequest.getDescription());
        existingTask.setDueDate(taskRequest.getDueDate());
        existingTask.setPriority(taskRequest.getPriority());
        existingTask.setStatus(taskRequest.getStatus());

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
