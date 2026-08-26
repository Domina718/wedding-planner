package com.example.weddingplanner.service;

import com.example.weddingplanner.model.Expense;
import com.example.weddingplanner.model.ExpenseCategory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ExpenseService {

    List<Expense> getExpensesForWedding(Long weddingId);

    List<Expense> getExpensesByCategory(Long weddingId, ExpenseCategory category);

    Optional<Expense> getExpenseById(Long id);

    Expense saveExpense(Expense expense);

    void deleteExpense(Long id);

    BigDecimal getTotalExpenses(Long weddingId);

    BigDecimal getTotalPaidExpenses(Long weddingId);

    Map<ExpenseCategory, BigDecimal> getExpenseByCategorySummary(Long weddingId);
}
