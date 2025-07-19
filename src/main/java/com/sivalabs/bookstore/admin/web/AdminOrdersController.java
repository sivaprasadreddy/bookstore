package com.sivalabs.bookstore.admin.web;

import com.sivalabs.bookstore.common.exceptions.ResourceNotFoundException;
import com.sivalabs.bookstore.common.model.PagedResult;
import com.sivalabs.bookstore.common.model.Pagination;
import com.sivalabs.bookstore.orders.OrdersAPI;
import com.sivalabs.bookstore.orders.core.models.OrderDto;
import com.sivalabs.bookstore.orders.core.models.OrderStatus;
import com.sivalabs.bookstore.orders.core.models.OrderSummary;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        var pagination = new Pagination<>(pagedResult, "/admin/orders");
        model.addAttribute("pagedResult", pagedResult);
        model.addAttribute("pagination", pagination);
        return "admin/orders";
    }

    @GetMapping("/orders/{orderNumber}")
    String showOrderDetails(@PathVariable String orderNumber, Model model) {
        OrderDto order = ordersAPI
                .findOrderByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderNumber));
        model.addAttribute("order", order);
        model.addAttribute("statuses", OrderStatus.values());
        return "admin/order-details";
    }

    @PutMapping("/orders/{orderNumber}/status")
    String updateOrderStatus(
            @PathVariable String orderNumber,
            @RequestParam OrderStatus status,
            @RequestParam(required = false) String comments,
            RedirectAttributes redirectAttributes) {
        try {
            ordersAPI.updateOrderStatus(orderNumber, status, comments);
            redirectAttributes.addFlashAttribute("message", "Order status updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update order status: " + e.getMessage());
        }
        return "redirect:/admin/orders/" + orderNumber;
    }
}
