package com.foodapp.dao;

import com.foodapp.entity.Payment;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PaymentDAO extends GenericDAO<Payment> {

    public PaymentDAO() {
        super(Payment.class);
    }

    public Payment findByOrderId(Long orderId) {
        List<Payment> results = em.createQuery("SELECT p FROM Payment p WHERE p.order.orderId = :oid", Payment.class)
                .setParameter("oid", orderId)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }
}