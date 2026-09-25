<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Register" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<main class="auth-container">
  <div class="auth-card">
    <h2>Create Your Account</h2>
    <p class="subtitle">Join FoodExpress and start ordering</p>

    <c:if test="${not empty error}">
      <div class="alert alert-error">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/register" class="auth-form">
      <div class="form-group">
        <label>Username</label>
        <input type="text" name="username" value="${username}" required placeholder="Choose a username">
      </div>
      <div class="form-group">
        <label>Email</label>
        <input type="email" name="email" value="${email}" required placeholder="your@email.com">
      </div>
      <div class="form-group">
        <label>Phone</label>
        <input type="tel" name="phone" value="${phone}" placeholder="+91 9xxxxxxxxx">
      </div>
      <div class="form-group">
        <label>I want to join as</label>
        <div class="role-picker">
          <label class="role-option">
            <input type="radio" name="role" value="CUSTOMER"
            ${empty role or role == 'CUSTOMER' ? 'checked' : ''}>
            <span class="role-box">
                <span class="role-emoji">🛍️</span>
                <span class="role-title">Customer</span>
                <span class="role-desc">Order food</span>
            </span>
          </label>
          <label class="role-option">
            <input type="radio" name="role" value="RESTAURANT_OWNER"
            ${role == 'RESTAURANT_OWNER' ? 'checked' : ''}>
            <span class="role-box">
                <span class="role-emoji">🍽️</span>
                <span class="role-title">Restaurant Owner</span>
                <span class="role-desc">List your restaurant</span>
            </span>
          </label>
        </div>
      </div>
      <div class="form-group">
        <label>Password</label>
        <input type="password" name="password" required placeholder="Min 6 characters">
      </div>
      <div class="form-group">
        <label>Confirm Password</label>
        <input type="password" name="confirmPassword" required placeholder="Repeat password">
      </div>
      <button type="submit" class="btn-primary btn-block">Register</button>
    </form>

    <p class="auth-footer">
      Already have an account?
      <a href="${pageContext.request.contextPath}/login">Login here</a>
    </p>
  </div>
</main>

<jsp:include page="../common/footer.jsp" />