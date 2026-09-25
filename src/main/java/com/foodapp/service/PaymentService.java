package com.foodapp.service;

import com.foodapp.dao.OrderDAO;
import com.foodapp.dao.PaymentDAO;
import com.foodapp.entity.Order;
import com.foodapp.entity.Payment;
import com.foodapp.enums.OrderStatus;
import com.foodapp.enums.PaymentMethod;
import com.foodapp.enums.PaymentStatus;
import com.foodapp.websocket.EventBroadcaster;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@ApplicationScoped
public class PaymentService {

    @Inject private PaymentDAO paymentDAO;
    @Inject private OrderDAO orderDAO;
    @Inject private EventBroadcaster eventBroadcaster;

    @Transactional
    public Payment processPayment(Long orderId, PaymentMethod method) {
        Order order = orderDAO.findById(orderId);
        if (order == null) throw new IllegalArgumentException("Order not found");

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(method);
        payment.setAmount(order.getFinalAmount());

        if (method == PaymentMethod.CASH_ON_DELIVERY) {
            payment.setStatus(PaymentStatus.PENDING);
        } else {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 12));
            payment.setPaidAt(LocalDateTime.now());
            order.setStatus(OrderStatus.CONFIRMED);
            orderDAO.update(order);
        }

        Payment saved = paymentDAO.save(payment);
        
        if (eventBroadcaster != null && order.getUser() != null) {
            eventBroadcaster.broadcastToUser(order.getUser().getUserId(), "{\"type\": \"PAYMENT_UPDATED\"}");
            eventBroadcaster.broadcastToUser(order.getUser().getUserId(), "{\"type\": \"ORDER_UPDATED\"}");
        }
        
        return saved;
    }
}