package com.example.weddingplanner.service;

import com.example.weddingplanner.model.Expense;
import com.example.weddingplanner.model.ExpenseCategory;
import com.example.weddingplanner.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService{

    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository){
        this.expenseRepository = expenseRepository;
    }

    @Override
    public List<Expense> getExpensesForWedding(Long weddingId){
        return expenseRepository.findByWeddingId(weddingId);
    }

    @Override
    public List<Expense> getExpensesByCategory(Long weddingId, ExpenseCategory category){
        return expenseRepository.findByWeddingIdAndCategory(weddingId, category);
    }

    @Override
    public Optional<Expense> getExpenseById(Long id, Long weddingId){
        return expenseRepository.findByIdAndWeddingId(id, weddingId);
    }

    @Override
    public Expense saveExpense(Expense expense){
        return expenseRepository.save(expense);
    }

    @Override
    public void deleteExpense(Long id, Long weddingId){
        expenseRepository.findByIdAndWeddingId(id, weddingId).ifPresent(expenseRepository::delete);
    }

    @Override
    public BigDecimal getTotalExpenses(Long weddingId){
        return getExpensesForWedding(weddingId)
                .stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal getTotalPaidExpenses(Long weddingId){
        return getExpensesForWedding(weddingId)
                .stream()
                .filter(Expense::isPaid)
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Map<ExpenseCategory, BigDecimal> getExpenseByCategorySummary(Long weddingId){
        return getExpensesForWedding(weddingId)
                .stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                Expense::getAmount,
                                BigDecimal::add
                        )
                ));
    }
}
