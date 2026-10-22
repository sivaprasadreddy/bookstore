package com.sivalabs.bookstore.orders.core;

import org.springframework.data.jpa.repository.JpaRepository;

interface PaymentRepository extends JpaRepository<Payment, Long> {}
