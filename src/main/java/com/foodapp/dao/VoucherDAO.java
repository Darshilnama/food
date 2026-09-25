package com.foodapp.dao;

import com.foodapp.entity.Voucher;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class VoucherDAO extends GenericDAO<Voucher> {

    public VoucherDAO() {
        super(Voucher.class);
    }

    public Voucher findByCode(String code) {
        List<Voucher> results = em.createQuery("SELECT v FROM Voucher v WHERE v.code = :code", Voucher.class)
                .setParameter("code", code)
                .getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    public List<Voucher> findActiveVouchers() {
        return em.createQuery(
                        "SELECT v FROM Voucher v WHERE v.isActive = true AND v.validUntil > :now", Voucher.class)
                .setParameter("now", LocalDateTime.now())
                .getResultList();
    }
}