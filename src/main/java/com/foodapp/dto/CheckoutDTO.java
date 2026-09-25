package com.foodapp.dto;

import com.foodapp.entity.Address;

import java.math.BigDecimal;
import java.util.List;

public class CheckoutDTO {

    private CartDTO cart;
    private List<Address> addresses;
    private Long selectedAddressId;

    private String voucherCode;
    private BigDecimal discount = BigDecimal.ZERO;
    private BigDecimal subtotal = BigDecimal.ZERO;
    private BigDecimal deliveryFee = BigDecimal.valueOf(30);
    private BigDecimal gst = BigDecimal.ZERO;
    private BigDecimal grandTotal = BigDecimal.ZERO;

    private String voucherError;
    private String voucherSuccess;
    private String generalError;

    public CartDTO getCart() { return cart; }
    public void setCart(CartDTO cart) { this.cart = cart; }
    public List<Address> getAddresses() { return addresses; }
    public void setAddresses(List<Address> addresses) { this.addresses = addresses; }
    public Long getSelectedAddressId() { return selectedAddressId; }
    public void setSelectedAddressId(Long selectedAddressId) { this.selectedAddressId = selectedAddressId; }
    public String getVoucherCode() { return voucherCode; }
    public void setVoucherCode(String voucherCode) { this.voucherCode = voucherCode; }
    public BigDecimal getDiscount() { return discount; }
    public void setDiscount(BigDecimal discount) { this.discount = discount; }
    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    public BigDecimal getDeliveryFee() { return deliveryFee; }
    public void setDeliveryFee(BigDecimal deliveryFee) { this.deliveryFee = deliveryFee; }
    public BigDecimal getGst() { return gst; }
    public void setGst(BigDecimal gst) { this.gst = gst; }
    public BigDecimal getGrandTotal() { return grandTotal; }
    public void setGrandTotal(BigDecimal grandTotal) { this.grandTotal = grandTotal; }
    public String getVoucherError() { return voucherError; }
    public void setVoucherError(String voucherError) { this.voucherError = voucherError; }
    public String getVoucherSuccess() { return voucherSuccess; }
    public void setVoucherSuccess(String voucherSuccess) { this.voucherSuccess = voucherSuccess; }
    public String getGeneralError() { return generalError; }
    public void setGeneralError(String generalError) { this.generalError = generalError; }
}