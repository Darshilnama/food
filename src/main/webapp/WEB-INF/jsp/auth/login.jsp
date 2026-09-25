<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Login" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<main class="auth-container">
  <div class="auth-card">
    <h2>Welcome Back</h2>
    <p class="subtitle">Sign in to order your favorite food</p>

    <c:if test="${not empty error}">
      <div class="alert alert-error">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/login" class="auth-form">
      <div class="form-group">
        <label>Username or Email</label>
        <input type="text" name="usernameOrEmail" value="${usernameOrEmail}"
               required autofocus placeholder="Enter username or email">
      </div>
      <div class="form-group">
        <label>Password</label>
        <input type="password" name="password" required placeholder="Enter password">
      </div>
      <button type="submit" class="btn-primary btn-block">Login</button>
    </form>

    <p class="auth-footer">
      Don't have an account?
      <a href="${pageContext.request.contextPath}/register">Register here</a>
    </p>
  </div>
</main>

<jsp:include page="../common/footer.jsp" />