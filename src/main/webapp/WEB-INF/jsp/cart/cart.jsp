<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Your Cart" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<main class="container">
    <c:if test="${not empty sessionScope.flashError}">
        <div class="alert alert-error">${sessionScope.flashError}</div>
        <c:remove var="flashError" scope="session" />
    </c:if>
    <h1 class="page-title">Your Cart</h1>

    <c:choose>
        <c:when test="${empty cart.items}">
            <div class="empty-state">
                <h3>Your cart is empty 🛒</h3>
                <p>Add some delicious food to get started.</p>
                <a href="${pageContext.request.contextPath}/restaurants" class="btn-primary">
                    Browse Restaurants
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="cart-layout">
                <div class="cart-items">
                    <div class="cart-restaurant">
                        <span>Ordering from: </span>
                        <strong>${cart.restaurantName}</strong>
                    </div>

                    <c:forEach var="item" items="${cart.items}">
                        <div class="cart-item" data-id="${item.cartItemId}">
                            <div class="cart-item-info">
                                <c:if test="${not empty item.imageUrl}">
                                    <img src="${item.imageUrl}" alt="${item.name}"
                                         class="cart-thumb"
                                         onerror="this.style.display='none'">
                                </c:if>
                                <span class="diet-indicator ${item.foodType == 'VEG' ? 'veg' : 'nonveg'}">
                                    <span class="diet-dot"></span>
                                </span>
                                <div class="cart-item-text">
                                    <span class="cart-item-name">${item.name}</span>
                                    <span class="cart-item-price">₹ ${item.unitPrice} each</span>
                                </div>
                            </div>

                            <div class="cart-item-controls">
                                <button class="qty-btn"
                                        onclick="updateQuantity(${item.cartItemId}, ${item.quantity - 1})">−</button>
                                <span class="qty-value">${item.quantity}</span>
                                <button class="qty-btn"
                                        onclick="updateQuantity(${item.cartItemId}, ${item.quantity + 1})">+</button>
                                <span class="line-total">₹ ${item.lineTotal}</span>
                                <button class="remove-btn"
                                        onclick="removeItem(${item.cartItemId})">✕</button>
                            </div>
                        </div>
                    </c:forEach>

                    <button class="clear-cart-btn" onclick="clearCart()">Clear Cart</button>
                </div>

                <div class="cart-summary">
                    <h3>Bill Summary</h3>
                    <div class="summary-row">
                        <span>Subtotal (${cart.totalItems} items)</span>
                        <span>₹ ${cart.subtotal}</span>
                    </div>
                    <div class="summary-row muted">
                        <span>Delivery Fee</span>
                        <span>₹ 30</span>
                    </div>
                    <div class="summary-row muted">
                        <span>GST (5%)</span>
                        <span>Calculated at checkout</span>
                    </div>
                    <div class="summary-row total">
                        <span>Estimated Total</span>
                        <span>₹ ${cart.subtotal + 30}</span>
                    </div>

                    <a href="${pageContext.request.contextPath}/checkout"
                       class="btn-primary btn-block checkout-btn">
                        Proceed to Checkout →
                    </a>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<div id="toast" class="toast"></div>
<script src="${pageContext.request.contextPath}/js/cart.js"></script>

<jsp:include page="../common/footer.jsp" />