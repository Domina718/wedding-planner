package com.example.weddingplanner.repository;

import com.example.weddingplanner.model.Expense;
import com.example.weddingplanner.model.ExpenseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByWeddingId(Long weddingId);

    List<Expense> findByWeddingIdAndCategory(Long weddingId, ExpenseCategory category);
}
