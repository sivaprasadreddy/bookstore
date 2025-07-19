package com.sivalabs.bookstore.admin.web;

import com.sivalabs.bookstore.common.model.PagedResult;
import com.sivalabs.bookstore.orders.OrdersAPI;
import com.sivalabs.bookstore.orders.core.models.OrderSummary;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
class AdminOrdersController {
    private final OrdersAPI ordersAPI;

    AdminOrdersController(OrdersAPI ordersAPI) {
        this.ordersAPI = ordersAPI;
    }

    @GetMapping("/orders")
    String showOrders(
            @RequestParam(name = "page", defaultValue = "1") int pageNo,
            @RequestParam(name = "size", defaultValue = "10") int pageSize,
            Model model) {
        PagedResult<OrderSummary> pagedResult = ordersAPI.findAllOrders(pageNo, pageSize);
        model.addAttribute("pagedResult", pagedResult);
        return "admin/orders";
    }
}
