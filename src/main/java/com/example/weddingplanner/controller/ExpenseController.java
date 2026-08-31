package com.example.weddingplanner.controller;

import com.example.weddingplanner.dto.ExpenseRequest;
import com.example.weddingplanner.exception.ResourceNotFoundException;
import com.example.weddingplanner.model.Expense;
import com.example.weddingplanner.model.ExpenseCategory;
import com.example.weddingplanner.model.TaskStatus;
import com.example.weddingplanner.model.Wedding;
import com.example.weddingplanner.service.ExpenseService;
import com.example.weddingplanner.service.WeddingService;
import jakarta.validation.Valid;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
        model.addAttribute("expenseRequest", new ExpenseRequest());
        model.addAttribute("categories", ExpenseCategory.values());

        model.addAttribute("budget", wedding.getBudget());
        model.addAttribute("totalExpenses", totalExpenses);
        model.addAttribute("remainingBudget", remainingBudget);

        model.addAttribute("totalPaid", expenseService.getTotalPaidExpenses(wedding.getId()));

        return "expenses";
    }

    @PostMapping("/expenses/save")
    public String saveExpense(@Valid @ModelAttribute("expenseRequest") ExpenseRequest expenseRequest,
                              BindingResult bindingResult,
                              Model model){

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding not found."));

        if(bindingResult.hasErrors()){
           BigDecimal totalExpenses = expenseService.getTotalExpenses(wedding.getId());

           BigDecimal remainingBudget = null;

           if(wedding.getBudget() != null){
               remainingBudget = wedding.getBudget().subtract(totalExpenses);
           }

           model.addAttribute("expenses", expenseService.getExpensesForWedding(wedding.getId()));

           model.addAttribute("categories", ExpenseCategory.values());
           model.addAttribute("budget", wedding.getBudget());
           model.addAttribute("totalExpenses", totalExpenses);
           model.addAttribute("remainingBudget", remainingBudget);
           model.addAttribute("totalPaid", expenseService.getTotalPaidExpenses(wedding.getId()));

           return "expenses";
        }

        Expense expense = new Expense();

        expense.setDescription(expenseRequest.getDescription());
        expense.setAmount(expenseRequest.getAmount());
        expense.setCategory(expenseRequest.getCategory());
        expense.setPaid(expenseRequest.isPaid());
        expense.setDate(expenseRequest.getDate());

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

        ExpenseRequest expenseRequest = new ExpenseRequest();

        expenseRequest.setId(expense.getId());
        expenseRequest.setDescription(expense.getDescription());
        expenseRequest.setAmount(expense.getAmount());
        expenseRequest.setCategory(expense.getCategory());
        expenseRequest.setPaid(expense.isPaid());
        expenseRequest.setDate(expense.getDate());

        model.addAttribute("expenseRequest", expenseRequest);
        model.addAttribute("categories", ExpenseCategory.values());

        return "expense-edit";
    }

    @PostMapping("/expenses/update")
    public String updateExpense(@Valid @ModelAttribute("expenseRequest") ExpenseRequest expenseRequest,
                                BindingResult bindingResult,
                                Model model){

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(()-> new IllegalStateException("Wedding not found."));

        if(bindingResult.hasErrors()){
            model.addAttribute("categories", ExpenseCategory.values());
            return "expense-edit";
        }

        Expense existingExpense = expenseService
                .getExpenseById(expenseRequest.getId(), wedding.getId())
                        .orElseThrow(()-> new ResourceNotFoundException("Expense not found."));

        existingExpense.setDescription(expenseRequest.getDescription());
        existingExpense.setAmount(expenseRequest.getAmount());
        existingExpense.setCategory(expenseRequest.getCategory());
        existingExpense.setDate(expenseRequest.getDate());
        existingExpense.setPaid(expenseRequest.isPaid());

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
