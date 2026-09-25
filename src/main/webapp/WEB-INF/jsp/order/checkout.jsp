<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Checkout" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<main class="container">
    <h1 class="page-title">Checkout</h1>

    <c:if test="${not empty checkout.generalError}">
        <div class="alert alert-error">${checkout.generalError}</div>
    </c:if>

    <div class="checkout-layout">

        <%-- LEFT: Address + Voucher --%>
        <div class="checkout-left">

            <%-- Address section --%>
            <section class="checkout-card">
                <h2>Delivery Address</h2>

                <c:choose>
                    <c:when test="${not empty checkout.addresses}">
                        <form method="post" action="${pageContext.request.contextPath}/checkout" id="orderForm">
                            <input type="hidden" name="action" value="placeOrder">
                            <input type="hidden" name="voucherCode" value="${checkout.voucherCode}">

                            <div class="address-list">
                                <c:forEach var="addr" items="${checkout.addresses}">
                                    <label class="address-option ${checkout.selectedAddressId == addr.addressId ? 'selected' : ''}">
                                        <input type="radio" name="addressId" value="${addr.addressId}"
                                            ${checkout.selectedAddressId == addr.addressId ? 'checked' : ''} required>
                                        <div class="address-info">
                                            <div class="address-label-row">
                                                <strong>${addr.label}</strong>
                                                <c:if test="${addr.isDefault}">
                                                    <span class="default-tag">Default</span>
                                                </c:if>
                                            </div>
                                            <p>${addr.street}</p>
                                            <p class="muted">${addr.city}${not empty addr.state ? ', '.concat(addr.state) : ''} ${addr.zipCode}</p>
                                        </div>
                                    </label>
                                </c:forEach>
                            </div>

                            <details class="add-address-toggle">
                                <summary>➕ Add a new address</summary>
                                <div class="address-form">
                                    <div class="form-row">
                                        <div class="form-group">
                                            <label>Label</label>
                                            <input type="text" name="label" placeholder="Home / Office">
                                        </div>
                                        <div class="form-group">
                                            <label>ZIP</label>
                                            <input type="text" name="zipCode">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label>Street</label>
                                        <input type="text" name="street">
                                    </div>
                                    <div class="form-row">
                                        <div class="form-group">
                                            <label>City</label>
                                            <input type="text" name="city">
                                        </div>
                                        <div class="form-group">
                                            <label>State</label>
                                            <input type="text" name="state">
                                        </div>
                                    </div>
                                    <button type="submit" name="action" value="addAddress"
                                            class="btn-secondary">Save Address</button>
                                </div>
                            </details>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <p class="muted">You have no saved addresses. Add one below.</p>
                        <form method="post" action="${pageContext.request.contextPath}/checkout">
                            <input type="hidden" name="action" value="addAddress">
                            <div class="form-row">
                                <div class="form-group">
                                    <label>Label</label>
                                    <input type="text" name="label" placeholder="Home / Office">
                                </div>
                                <div class="form-group">
                                    <label>ZIP</label>
                                    <input type="text" name="zipCode">
                                </div>
                            </div>
                            <div class="form-group">
                                <label>Street</label>
                                <input type="text" name="street" required>
                            </div>
                            <div class="form-row">
                                <div class="form-group">
                                    <label>City</label>
                                    <input type="text" name="city" required>
                                </div>
                                <div class="form-group">
                                    <label>State</label>
                                    <input type="text" name="state">
                                </div>
                            </div>
                            <button type="submit" class="btn-primary">Save Address</button>
                        </form>
                    </c:otherwise>
                </c:choose>
            </section>

            <%-- Voucher section --%>
            <section class="checkout-card">
                <h2>Have a Voucher?</h2>
                <form method="post" action="${pageContext.request.contextPath}/checkout" class="voucher-form">
                    <input type="hidden" name="action" value="applyVoucher">
                    <input type="text" name="voucherCode" value="${checkout.voucherCode}"
                           placeholder="Enter code (try SAVE10, FLAT50)">
                    <button type="submit" class="btn-secondary">Apply</button>
                </form>

                <c:if test="${not empty checkout.voucherError}">
                    <div class="alert alert-error small">${checkout.voucherError}</div>
                </c:if>
                <c:if test="${not empty checkout.voucherSuccess}">
                    <div class="alert alert-success small">${checkout.voucherSuccess}</div>
                </c:if>
            </section>

        </div>

        <%-- RIGHT: Order Summary --%>
        <div class="checkout-right">
            <div class="cart-summary">
                <h3>Order Summary</h3>

                <div class="summary-items">
                    <c:forEach var="item" items="${checkout.cart.items}">
                        <div class="summary-item">
                            <span>${item.name} × ${item.quantity}</span>
                            <span>₹ ${item.lineTotal}</span>
                        </div>
                    </c:forEach>
                </div>

                <div class="summary-row">
                    <span>Subtotal</span>
                    <span>₹ ${checkout.subtotal}</span>
                </div>

                <c:if test="${checkout.discount > 0}">
                    <div class="summary-row discount">
                        <span>Discount (${checkout.voucherCode})</span>
                        <span>− ₹ ${checkout.discount}</span>
                    </div>
                </c:if>

                <div class="summary-row muted">
                    <span>Delivery Fee</span>
                    <span>₹ ${checkout.deliveryFee}</span>
                </div>

                <div class="summary-row muted">
                    <span>GST (5%)</span>
                    <span>₹ ${checkout.gst}</span>
                </div>

                <div class="summary-row total">
                    <span>Total</span>
                    <span>₹ ${checkout.grandTotal}</span>
                </div>

                <button type="submit" form="orderForm" class="btn-primary btn-block checkout-btn">
                    Place Order →
                </button>
            </div>
        </div>
    </div>
</main>

<jsp:include page="../common/footer.jsp" />