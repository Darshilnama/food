<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<nav class="navbar">
  <div class="nav-container">
    <a href="${pageContext.request.contextPath}/" class="brand">
      <span class="brand-icon">🍔</span>
      <span class="brand-name">Food<span class="brand-accent">Express</span></span>
    </a>

    <c:choose>
      <c:when test="${sessionScope.user != null}">
        <div class="nav-links">
          <c:if test="${sessionScope.role != 'RESTAURANT_OWNER'}">
            <a href="${pageContext.request.contextPath}/restaurants" class="nav-link">
              <span class="nav-icon"></span> Restaurants
            </a>
            <a href="${pageContext.request.contextPath}/orders" class="nav-link">
              <span class="nav-icon"></span> My Orders
            </a>
          </c:if>

          <c:if test="${sessionScope.role == 'ADMIN'}">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link">
              <span class="nav-icon"></span> Admin
            </a>
          </c:if>

          <c:if test="${sessionScope.role == 'RESTAURANT_OWNER'}">
            <a href="${pageContext.request.contextPath}/owner/dashboard" class="nav-link">
              <span class="nav-icon"></span> Owner Panel
            </a>
          </c:if>

          <c:if test="${sessionScope.role != 'RESTAURANT_OWNER'}">
<%--            <a href="${pageContext.request.contextPath}/preference/toggle-veg"--%>
<%--               class="veg-toggle ${sessionScope.vegMode ? 'veg-active' : ''}"--%>
<%--               title="Toggle Veg Mode">--%>
<%--              <span class="veg-dot"></span>--%>
<%--                ${sessionScope.vegMode ? 'Veg' : 'All'}--%>
<%--            </a>--%>

            <a href="${pageContext.request.contextPath}/cart" class="cart-link">
              <span class="cart-icon">🛒</span>
              <span>Cart</span>
              <span id="cart-badge"
                    class="cart-badge ${empty sessionScope.cartCount or sessionScope.cartCount == 0 ? 'hidden' : ''}">
                  ${sessionScope.cartCount != null ? sessionScope.cartCount : 0}
              </span>
            </a>
          </c:if>

          <div class="user-menu">
            <span class="user-avatar">${sessionScope.username.substring(0,1).toUpperCase()}</span>
            <span class="user-name">${sessionScope.username}</span>
          </div>

          <a href="${pageContext.request.contextPath}/logout" class="btn-logout">
            Logout
          </a>
        </div>
      </c:when>
      <c:otherwise>
        <div class="nav-links">
          <a href="${pageContext.request.contextPath}/login" class="nav-link">Login</a>
          <a href="${pageContext.request.contextPath}/register" class="btn-primary nav-cta">Sign Up</a>
        </div>
      </c:otherwise>
    </c:choose>
  </div>
</nav>