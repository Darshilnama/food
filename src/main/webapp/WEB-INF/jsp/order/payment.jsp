<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Payment" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<main class="container narrow">
  <h1 class="page-title">Payment</h1>

  <c:if test="${not empty error}">
    <div class="alert alert-error">${error}</div>
  </c:if>

  <div class="payment-card">
    <div class="payment-order-info">
      <p class="muted">Order #${order.orderId}</p>
      <p class="payment-amount">₹ ${order.finalAmount}</p>
    </div>

    <form method="post" action="${pageContext.request.contextPath}/payment">
      <input type="hidden" name="orderId" value="${order.orderId}">

      <h3>Select Payment Method</h3>
      <div class="payment-methods">
        <c:forEach var="m" items="${methods}">
          <label class="payment-option">
            <input type="radio" name="method" value="${m}" required>
            <span class="payment-box">
                <span class="payment-initial ${m == 'CASH_ON_DELIVERY' ? 'cash' : ''}">
                    <c:choose>
                      <c:when test="${m == 'CARD'}">C</c:when>
                      <c:when test="${m == 'UPI'}">U</c:when>
                      <c:when test="${m == 'NET_BANKING'}">N</c:when>
                      <c:when test="${m == 'WALLET'}">W</c:when>
                      <c:when test="${m == 'CASH_ON_DELIVERY'}">₹</c:when>
                      <c:otherwise>?</c:otherwise>
                    </c:choose>
                </span>
                <span class="payment-label">
                    <c:choose>
                      <c:when test="${m == 'CARD'}">Credit / Debit Card</c:when>
                      <c:when test="${m == 'UPI'}">UPI Payment</c:when>
                      <c:when test="${m == 'NET_BANKING'}">Net Banking</c:when>
                      <c:when test="${m == 'WALLET'}">Wallet</c:when>
                      <c:when test="${m == 'CASH_ON_DELIVERY'}">Cash on Delivery</c:when>
                      <c:otherwise>${m}</c:otherwise>
                    </c:choose>
                </span>
            </span>
          </label>
        </c:forEach>
      </div>

      <button type="submit" class="btn-primary btn-block checkout-btn">
        Pay ₹ ${order.finalAmount}
      </button>
    </form>
  </div>
</main>

<jsp:include page="../common/footer.jsp" />