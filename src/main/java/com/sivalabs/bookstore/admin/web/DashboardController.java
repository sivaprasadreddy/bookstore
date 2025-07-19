package com.sivalabs.bookstore.admin.web;

import com.sivalabs.bookstore.orders.OrdersAPI;
import com.sivalabs.bookstore.orders.core.models.OrderStats;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
class DashboardController {

    private final OrdersAPI ordersAPI;

    DashboardController(OrdersAPI ordersAPI) {
        this.ordersAPI = ordersAPI;
    }

    @GetMapping("")
    String dashboard(Model model) {
        OrderStats orderStats = ordersAPI.getOrderStats();
        model.addAttribute("orderStats", orderStats);
        return "admin/dashboard";
    }
}
