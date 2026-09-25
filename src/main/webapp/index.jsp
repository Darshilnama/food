<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="FoodExpress" />
<jsp:include page="/WEB-INF/jsp/common/header.jsp" />
<jsp:include page="/WEB-INF/jsp/common/navbar.jsp" />

<main class="hero">
    <div class="hero-content">
        <h1>Delicious food, delivered fast 🍕</h1>
        <p>Order from your favorite restaurants in minutes.</p>
        <c:choose>
            <c:when test="${sessionScope.user != null}">
                <a href="${pageContext.request.contextPath}/restaurants" class="btn-primary">Browse Restaurants</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/register" class="btn-primary">Get Started</a>
                <a href="${pageContext.request.contextPath}/login" class="btn-secondary">Login</a>
            </c:otherwise>
        </c:choose>
    </div>
</main>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />