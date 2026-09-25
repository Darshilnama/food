<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Bill" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<main class="container narrow">
  <div class="bill-success">
    <div class="success-check">✓</div>
    <h1>Payment Successful</h1>
    <p>Thank you, ${bill.customerName}! Your order is confirmed.</p>
  </div>

  <div class="bill-card">
    <div class="bill-header">
      <div>
        <h2>${bill.restaurantName}</h2>
        <p class="muted">Order #${bill.orderId}</p>
      </div>
      <div class="bill-date">
        <p class="muted">${bill.orderDate}</p>
      </div>
    </div>

    <div class="bill-address">
      <strong>Deliver to:</strong>
      <p>${bill.deliveryAddress}</p>
    </div>

    <table class="bill-table">
      <thead>
      <tr>
        <th>Item</th>
        <th class="right">Qty</th>
        <th class="right">Price</th>
        <th class="right">Total</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="i" items="${bill.items}">
        <tr>
          <td>${i.itemName}</td>
          <td class="right">${i.quantity}</td>
          <td class="right">₹ ${i.unitPrice}</td>
          <td class="right">₹ ${i.unitPrice * i.quantity}</td>
        </tr>
      </c:forEach>
      </tbody>
    </table>

    <div class="bill-totals">
      <div class="summary-row">
        <span>Subtotal</span><span>₹ ${bill.subtotal}</span>
      </div>
      <c:if test="${bill.discount > 0}">
        <div class="summary-row discount">
          <span>Discount</span><span>− ₹ ${bill.discount}</span>
        </div>
      </c:if>
      <div class="summary-row muted">
        <span>GST (5%)</span><span>₹ ${bill.gst}</span>
      </div>
      <div class="summary-row muted">
        <span>Delivery Fee</span><span>₹ ${bill.deliveryFee}</span>
      </div>
      <div class="summary-row total">
        <span>Grand Total</span><span>₹ ${bill.grandTotal}</span>
      </div>
    </div>
  </div>

  <div class="bill-actions">
    <a href="${pageContext.request.contextPath}/orders" class="btn-secondary">View My Orders</a>
    <a href="${pageContext.request.contextPath}/restaurants" class="btn-primary">Order More Food</a>
  </div>
</main>

<jsp:include page="../common/footer.jsp" />