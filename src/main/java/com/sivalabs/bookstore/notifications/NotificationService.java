package com.sivalabs.bookstore.notifications;

import com.sivalabs.bookstore.orders.domain.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationService {

    public void sendConfirmationNotification(Order order) {
        String email = """
                Hi %s,
                This email is to notify you that your order : %s is received and will be processed soon.
                
                Thanks,
                BookStore Team
                """.formatted(order.getCustomerName(), order.getOrderId());
        log.info("==========================================================");
        log.info("                    Order Confirmation           ");
        log.info("==========================================================");
        log.info(email);
        log.info("==========================================================");
    }

    public void sendDeliveredNotification(Order order) {
        String email = """
                Hi %s,
                This email is to notify you that your order : %s is delivered.
                
                Thanks,
                BookStore Team
                """.formatted(order.getCustomerName(), order.getOrderId());
        log.info("==========================================================");
        log.info("                    Order Delivery Confirmation           ");
        log.info("==========================================================");
        log.info(email);
        log.info("==========================================================");
    }

    public void sendCancelledNotification(Order order) {
        String email = """
                Hi %s,
                This email is to notify you that your order : %s is cancelled.
                
                Thanks,
                BookStore Team
                """.formatted(order.getCustomerName(), order.getOrderId());
        log.info("==========================================================");
        log.info("                    Order Cancellation Notification       ");
        log.info("==========================================================");
        log.info(email);
        log.info("==========================================================");
    }


}
