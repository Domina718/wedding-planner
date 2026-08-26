package com.example.weddingplanner.controller;

import com.example.weddingplanner.model.ExpenseCategory;
import com.example.weddingplanner.model.Wedding;
import com.example.weddingplanner.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@Controller
public class WeddingController {

    private final WeddingService weddingService;
    private final GuestService guestService;
    private final TaskService taskService;
    private final ExpenseService expenseService;
    private final VendorService vendorService;

    public WeddingController(WeddingService weddingService,
                             GuestService guestService,
                             TaskService taskService,
                             ExpenseService expenseService,
                             VendorService vendorService){

        this.weddingService = weddingService;
        this.guestService = guestService;
        this.taskService = taskService;
        this.expenseService = expenseService;
        this.vendorService = vendorService;
    }

    @GetMapping("/")
    public String home(Model model){
        Wedding wedding = weddingService.getWedding().orElse(new Wedding());

        model.addAttribute("wedding", wedding);

        if (wedding.getId() != null){
            Long weddingId = wedding.getId();

            model.addAttribute("guestCount", guestService.countGuests(weddingId));

            model.addAttribute("confirmedGuestCount", guestService.countConfirmedGuests(weddingId));

            model.addAttribute("taskCount", taskService.countTasks(weddingId));

            model.addAttribute("completedTaskCount", taskService.countCompletedTasks(weddingId));

            model.addAttribute("vendorCount", vendorService.countVendors(weddingId));

            model.addAttribute("bookedVendorCount", vendorService.countBookedVendors(weddingId));

            BigDecimal totalExpenses = expenseService.getTotalExpenses(weddingId);

            model.addAttribute("totalExpenses", totalExpenses);

            Map<ExpenseCategory, BigDecimal> expensesByCategory = expenseService.getExpenseByCategorySummary(weddingId);

            model.addAttribute("expenseCategoryLabels",
                    expensesByCategory.keySet()
                            .stream()
                            .map(Enum::name)
                            .toList());

            model.addAttribute("expenseCategoryValues", expensesByCategory.values());

            model.addAttribute("declinedGuestCount", guestService.countDeclinedGuests(weddingId));

            model.addAttribute("pendingGuestCount", guestService.countPendingGuests(weddingId));

            if(wedding.getBudget() != null){
                model.addAttribute("remainingBudget", wedding.getBudget().subtract(totalExpenses));
            }

            if(wedding.getWeddingDate() != null){
                long daysUntilWedding = ChronoUnit.DAYS.between(LocalDate.now(), wedding.getWeddingDate());

                model.addAttribute("daysUntilWedding", daysUntilWedding);
            }

        }

        return "index";
    }

    @PostMapping("/wedding/save")
    public String saveWedding(Wedding wedding){
        weddingService.saveWedding(wedding);

        return "redirect:/";
    }
}
