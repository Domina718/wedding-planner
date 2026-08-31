package com.example.weddingplanner.dto;

import com.example.weddingplanner.model.TaskPriority;
import com.example.weddingplanner.model.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TaskRequest {

    private Long id;

    @NotBlank(message = "Title is required.")
    private String title;

    private String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Due date is required.")
    private LocalDate dueDate;

    @NotNull(message = "Priority is required.")
    private TaskPriority priority;

    @NotNull(message = "Status is required.")
    private TaskStatus status;
}
