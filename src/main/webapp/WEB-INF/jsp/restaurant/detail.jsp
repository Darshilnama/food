<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="${restaurant.name}" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<main class="container">

    <div class="restaurant-hero">
        <img src="${restaurant.imageUrl}" alt="${restaurant.name}"
             onerror="this.src='https://via.placeholder.com/1200x300?text=Restaurant'">
        <div class="restaurant-hero-info">
            <h1>${restaurant.name}</h1>
            <p class="cuisine">${restaurant.cuisineType} • ${restaurant.address}</p>
            <div class="hero-meta">
                <span>⭐ ${restaurant.rating} (${restaurant.totalReviews})</span>
                <span>🕐 ${restaurant.avgDeliveryTime} min</span>
                <span>📍 ${restaurant.distanceKm} km</span>
                <c:if test="${restaurant.vegOnly}">
                    <span class="badge-veg-inline">🌱 Pure Veg</span>
                </c:if>
            </div>
        </div>
    </div>

    <div class="menu-header">
        <h2 class="section-title">Menu</h2>
        <c:if test="${sessionScope.vegMode}">
            <span class="veg-filter-indicator">🌱 Showing veg items only</span>
        </c:if>
    </div>

    <c:choose>
        <c:when test="${empty menuItems}">
            <div class="empty-state">
                <p>No items available right now.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="menu-list">
                <c:forEach var="item" items="${menuItems}">
                    <c:if test="${not sessionScope.vegMode or item.foodType == 'VEG'}">
                        <div class="menu-item">
                            <div class="menu-item-thumb">
                                <c:choose>
                                    <c:when test="${not empty item.imageUrl}">
                                        <img src="${item.imageUrl}" alt="${item.name}"
                                             onerror="this.src='https://via.placeholder.com/100x100?text=Food'">
                                    </c:when>
                                    <c:otherwise>
                                        <img src="https://via.placeholder.com/100x100?text=Food" alt="No image">
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <div class="menu-item-info">
                                <div class="menu-item-name-row">
                                    <span class="diet-indicator ${item.foodType == 'VEG' ? 'veg' : 'nonveg'}">
                                        <span class="diet-dot"></span>
                                    </span>
                                    <h3>${item.name}</h3>
                                </div>
                                <p class="item-desc">${item.description}</p>
                                <p class="item-price">₹ ${item.price}</p>
                            </div>

                            <div class="menu-item-action">
                                <button class="btn-primary add-to-cart-btn"
                                        data-item-id="${item.itemId}"
                                        data-item-name="${item.name}">
                                    Add to Cart
                                </button>
                            </div>
                        </div>
                    </c:if>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>

</main>

<div id="toast" class="toast"></div>
<script src="${pageContext.request.contextPath}/js/cart.js"></script>

<jsp:include page="../common/footer.jsp" />