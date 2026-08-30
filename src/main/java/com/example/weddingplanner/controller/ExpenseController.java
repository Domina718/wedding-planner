package com.example.weddingplanner.controller;

import com.example.weddingplanner.exception.ResourceNotFoundException;
import com.example.weddingplanner.model.Expense;
import com.example.weddingplanner.model.ExpenseCategory;
import com.example.weddingplanner.model.Wedding;
import com.example.weddingplanner.service.ExpenseService;
import com.example.weddingplanner.service.WeddingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class ExpenseController {

    private final ExpenseService expenseService;
    private final WeddingService weddingService;

    public ExpenseController(ExpenseService expenseService, WeddingService weddingService){
        this.expenseService = expenseService;
        this.weddingService = weddingService;
    }

    @GetMapping("/expenses")
    public String showExpense(@RequestParam(required = false)ExpenseCategory category, Model model){
        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding not found."));

        BigDecimal totalExpenses = expenseService.getTotalExpenses(wedding.getId());

        BigDecimal remainingBudget = null;

        if(wedding.getBudget() != null){
            remainingBudget = wedding.getBudget().subtract(totalExpenses);
        }

        List<Expense> expenses;

        if(category != null){
            expenses = expenseService.getExpensesByCategory(wedding.getId(), category);
        }
        else{
            expenses = expenseService.getExpensesForWedding(wedding.getId());
        }

        model.addAttribute("expenses", expenses);
        model.addAttribute("expense", new Expense());
        model.addAttribute("categories", ExpenseCategory.values());

        model.addAttribute("budget", wedding.getBudget());
        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("remainingBudget", remainingBudget);

        model.addAttribute("totalPaid", expenseService.getTotalPaidExpenses(wedding.getId()));

        return "expenses";
    }

    @PostMapping("/expenses/save")
    public String saveExpense(@ModelAttribute Expense expense){

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding not found."));

        expense.setWedding(wedding);
        expenseService.saveExpense(expense);

        return "redirect:/expenses";
    }

    @GetMapping("/expenses/edit/{id}")
    public String editExpense(@PathVariable Long id, Model model){

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding not found."));

        Expense expense = expenseService.getExpenseById(id, wedding.getId())
                .orElseThrow(()-> new ResourceNotFoundException("Expense not found."));

        model.addAttribute("expense", expense);
        model.addAttribute("categories", ExpenseCategory.values());

        return "expense-edit";
    }

    @PostMapping("/expenses/update")
    public String updateExpense(@ModelAttribute Expense expense){

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding not found."));

        Expense existingExpense = expenseService
                .getExpenseById(expense.getId(), wedding.getId())
                        .orElseThrow(()-> new ResourceNotFoundException("Expense not found."));

        existingExpense.setDescription(expense.getDescription());
        existingExpense.setAmount(expense.getAmount());
        existingExpense.setCategory(expense.getCategory());
        existingExpense.setDate(expense.getDate());
        existingExpense.setPaid(expense.isPaid());

        expenseService.saveExpense(existingExpense);

        return "redirect:/expenses";
    }

    @PostMapping("/expenses/delete/{id}")
    public String deleteExpense(@PathVariable Long id){

        Wedding wedding = weddingService.getWedding()
                        .orElseThrow(()-> new IllegalStateException("Wedding not found."));

        expenseService.deleteExpense(id, wedding.getId());

        return "redirect:/expenses";
    }


}
