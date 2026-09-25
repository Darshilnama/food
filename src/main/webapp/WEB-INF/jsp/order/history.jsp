<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="My Orders" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<main class="container">
    <h1 class="page-title">My Orders</h1>

    <c:choose>
        <c:when test="${empty orders}">
            <div class="empty-state">
                <h3>No orders yet 🍽️</h3>
                <p>Your order history will appear here.</p>
                <a href="${pageContext.request.contextPath}/restaurants" class="btn-primary">
                    Browse Restaurants
                </a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="orders-list">
                <c:forEach var="o" items="${orders}">
                    <div class="order-card">
                        <div class="order-card-header">
                            <div>
                                <h3>${o.restaurant.name}</h3>
                                <p class="muted small">Order #${o.orderId} • ${o.createdAt}</p>
                            </div>
                            <span class="status-badge status-${o.status}">${o.status}</span>
                        </div>
                        <div class="order-card-body">
                            <div class="order-items-preview">
                                <c:forEach var="i" items="${o.items}" varStatus="loop">
                                    <span>${i.itemName} × ${i.quantity}<c:if test="${!loop.last}">, </c:if></span>
                                </c:forEach>
                            </div>
                            <div class="order-total">
                                <span class="muted">Total</span>
                                <strong>₹ ${o.finalAmount}</strong>
                            </div>
                        </div>
                        <div class="order-card-actions">
                            <a href="${pageContext.request.contextPath}/bill?orderId=${o.orderId}"
                               class="btn-small btn-edit">View Bill</a>
                            <c:if test="${o.status == 'PENDING'}">
                                <a href="${pageContext.request.contextPath}/payment?orderId=${o.orderId}"
                                   class="btn-small btn-primary">Pay Now</a>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<jsp:include page="../common/footer.jsp" />