package com.foodapp.service;

import com.foodapp.dao.VoucherDAO;
import com.foodapp.entity.Voucher;
import com.foodapp.enums.DiscountType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@ApplicationScoped
public class VoucherService {

    @Inject
    private VoucherDAO voucherDAO;

    /**
     * Validates a voucher and returns the discount amount for the given subtotal.
     * Returns BigDecimal.ZERO if the voucher is invalid or not applicable.
     */
    public BigDecimal calculateDiscount(String code, BigDecimal subtotal) {
        if (code == null || code.isBlank()) return BigDecimal.ZERO;

        Voucher voucher = voucherDAO.findByCode(code);
        if (voucher == null || !voucher.getIsActive()) return BigDecimal.ZERO;
        if (voucher.getValidUntil() != null && voucher.getValidUntil().isBefore(LocalDateTime.now()))
            return BigDecimal.ZERO;
        if (voucher.getMinOrderAmount() != null && subtotal.compareTo(voucher.getMinOrderAmount()) < 0)
            return BigDecimal.ZERO;

        BigDecimal discount;
        if (voucher.getDiscountType() == DiscountType.PERCENTAGE) {
            discount = subtotal.multiply(voucher.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            discount = voucher.getDiscountValue();
        }

        if (voucher.getMaxDiscount() != null && discount.compareTo(voucher.getMaxDiscount()) > 0) {
            discount = voucher.getMaxDiscount();
        }
        if (discount.compareTo(subtotal) > 0) discount = subtotal;

        return discount;
    }

    public Voucher findValidVoucher(String code) {
        Voucher v = voucherDAO.findByCode(code);
        if (v == null || !v.getIsActive()) return null;
        if (v.getValidUntil() != null && v.getValidUntil().isBefore(LocalDateTime.now())) return null;
        return v;
    }
}