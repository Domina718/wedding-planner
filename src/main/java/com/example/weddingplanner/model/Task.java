package com.example.weddingplanner.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @ManyToOne
    @JoinColumn(name = "wedding_id")
    private Wedding wedding;

    @Transient
    public boolean isOverdue(){
        return dueDate != null && dueDate.isBefore(LocalDate.now())
                && status != TaskStatus.COMPLETED;
    }
}
