package com.sivalabs.bookstore.orders.core.models;

/**
 * Record to hold order statistics for the admin dashboard.
 */
public record OrderStats(long totalOrders, long deliveredOrders, long pendingOrders, long cancelledOrders) {}
