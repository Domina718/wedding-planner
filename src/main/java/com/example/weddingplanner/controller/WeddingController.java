package com.example.weddingplanner.controller;

import com.example.weddingplanner.dto.WeddingRequest;
import com.example.weddingplanner.model.ExpenseCategory;
import com.example.weddingplanner.model.Wedding;
import com.example.weddingplanner.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
                             VendorService vendorService) {

        this.weddingService = weddingService;
        this.guestService = guestService;
        this.taskService = taskService;
        this.expenseService = expenseService;
        this.vendorService = vendorService;
    }

    @GetMapping("/")
    public String home(Model model) {

        Wedding wedding = weddingService.getWedding().orElse(new Wedding());

        populateDashboard(model, wedding);

        WeddingRequest weddingRequest = new WeddingRequest();

        weddingRequest.setPartnerOneName(wedding.getPartnerOneName());
        weddingRequest.setPartnerTwoName(wedding.getPartnerTwoName());
        weddingRequest.setWeddingDate(wedding.getWeddingDate());
        weddingRequest.setBudget(wedding.getBudget());

        model.addAttribute("weddingRequest", weddingRequest);

        return "index";
    }

    @PostMapping("/wedding/save")
    public String saveWedding(@Valid @ModelAttribute("weddingRequest") WeddingRequest weddingRequest,
                              BindingResult bindingResult,
                              Model model) {

        Wedding wedding = weddingService.getWedding()
                .orElseThrow(() -> new IllegalStateException("Wedding not found."));

        if (bindingResult.hasErrors()) {

            populateDashboard(model, wedding);
            return "index";
        }

        wedding.setPartnerOneName(weddingRequest.getPartnerOneName());
        wedding.setPartnerTwoName(weddingRequest.getPartnerTwoName());
        wedding.setWeddingDate(weddingRequest.getWeddingDate());
        wedding.setBudget(weddingRequest.getBudget());

        weddingService.saveWedding(wedding);

        return "redirect:/";
    }

    private void populateDashboard(Model model, Wedding wedding) {

        model.addAttribute("wedding", wedding);

        if (wedding.getId() == null) {
            return;
        }

        Long weddingId = wedding.getId();

        long guestCount = guestService.countGuests(weddingId);
        long confirmedGuestCount = guestService.countConfirmedGuests(weddingId);

        model.addAttribute("guestCount", guestCount);

        model.addAttribute("confirmedGuestCount", confirmedGuestCount);

        model.addAttribute("declinedGuestCount", guestService.countDeclinedGuests(weddingId));

        model.addAttribute("invitedGuestCount", guestService.countInvitedGuests(weddingId));

        int rsvpConfirmedPercentage = 0;

        if(guestCount > 0){
            rsvpConfirmedPercentage = (int) Math.round((confirmedGuestCount * 100.0) / guestCount);
        }

        model.addAttribute("rsvpConfirmedPercentage", rsvpConfirmedPercentage);


        long taskCount = taskService.countTasks(weddingId);
        long completedTaskCount = taskService.countCompletedTasks(weddingId);

        model.addAttribute("taskCount", taskCount);

        model.addAttribute("completedTaskCount",completedTaskCount);

        int taskCompletionPercentage = 0;
        if(taskCount > 0){
            taskCompletionPercentage = (int) Math.round((completedTaskCount * 100.0) / taskCount);
        }

        model.addAttribute("taskCompletionPercentage", taskCompletionPercentage);

        model.addAttribute("upcomingTaskCount", taskService.countUpcomingTasks(weddingId));

        model.addAttribute("overdueTaskCount", taskService.countOverdueTasks(weddingId));


        model.addAttribute("vendorCount", vendorService.countVendors(weddingId));

        model.addAttribute("bookedVendorCount", vendorService.countBookedVendors(weddingId));

        model.addAttribute("vendorRemainingAmount", vendorService.getTotalRemainingAmount(weddingId));


        BigDecimal totalExpenses = expenseService.getTotalExpenses(weddingId);

        model.addAttribute("totalExpenses", totalExpenses);

        Map<ExpenseCategory, BigDecimal> expensesByCategory = expenseService.getExpenseByCategorySummary(weddingId);

        model.addAttribute("expenseCategoryLabels",
                expensesByCategory.keySet()
                        .stream()
                        .map(Enum::name)
                        .toList());

        model.addAttribute("expenseCategoryValues", expensesByCategory.values());

        if (wedding.getBudget() != null) {

            BigDecimal remainingBudget = wedding.getBudget().subtract(totalExpenses);

            model.addAttribute("remainingBudget", remainingBudget);

            int budgetUsedPercentage = 0;

            if(wedding.getBudget().compareTo(BigDecimal.ZERO) > 0){

                budgetUsedPercentage = totalExpenses
                        .multiply(BigDecimal.valueOf(100))
                        .divide(wedding.getBudget(), 0, RoundingMode.HALF_UP)
                        .intValue();
            }

            model.addAttribute("budgetUsedPercentage", budgetUsedPercentage);

            model.addAttribute("budgetProgressWidth", Math.min(budgetUsedPercentage, 100));
        }

        if (wedding.getWeddingDate() != null) {
            long daysUntilWedding = ChronoUnit.DAYS.between(LocalDate.now(), wedding.getWeddingDate());

            model.addAttribute("daysUntilWedding", daysUntilWedding);
        }
    }

}
