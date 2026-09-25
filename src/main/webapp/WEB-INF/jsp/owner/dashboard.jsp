<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Owner Dashboard" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<main class="container">
    <c:if test="${param.success == '1'}">
        <div class="alert alert-success">✅ Saved successfully!</div>
    </c:if>

    <c:choose>
        <c:when test="${empty restaurant}">
            <div class="owner-welcome">
                <h1>Welcome, ${sessionScope.username} 👋</h1>
                <p>You haven't registered your restaurant yet.</p>
                <p>Once registered, your restaurant will immediately appear on the customer's browse page.</p>
                <a href="${pageContext.request.contextPath}/owner/restaurant" class="btn-primary">
                    ➕ Register My Restaurant
                </a>
            </div>
        </c:when>

        <c:otherwise>
            <div class="owner-header">
                <div>
                    <h1>${restaurant.name}</h1>
                    <p class="subtitle">${restaurant.cuisineType} • ${restaurant.address}</p>
                </div>
                <a href="${pageContext.request.contextPath}/owner/restaurant" class="btn-secondary">
                     Edit Restaurant
                </a>
            </div>

            <div class="owner-stats">
                <div class="stat-card">
                    <span class="stat-value">⭐ ${restaurant.rating}</span>
                    <span class="stat-label">${restaurant.totalReviews} reviews</span>
                </div>
                <div class="stat-card">
                    <span class="stat-value">${menuItems.size()}</span>
                    <span class="stat-label">Menu items</span>
                </div>
                <div class="stat-card">
                    <span class="stat-value">${restaurant.avgDeliveryTime} min</span>
                    <span class="stat-label">Avg delivery</span>
                </div>
                <div class="stat-card">
                    <span class="stat-value">${restaurant.isOpen ? 'Open' : 'Closed'}</span>
                    <span class="stat-label">Status</span>
                </div>
            </div>

            <div class="menu-section-header">
                <h2>Menu Items</h2>
                <a href="${pageContext.request.contextPath}/owner/menu-item" class="btn-primary">
                    ➕ Add Menu Item
                </a>
            </div>

            <c:choose>
                <c:when test="${empty menuItems}">
                    <div class="empty-state">
                        <h3>No menu items yet</h3>
                        <p>Add your first item to attract customers.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="owner-menu-list">
                        <c:forEach var="item" items="${menuItems}">
                            <div class="owner-menu-row">
                                <div class="owner-menu-info">
                                    <c:if test="${not empty item.imageUrl}">
                                        <img src="${item.imageUrl}" alt="${item.name}"
                                             class="owner-thumb"
                                             onerror="this.style.display='none'">
                                    </c:if>
                                    <span class="diet-indicator ${item.foodType == 'VEG' ? 'veg' : 'nonveg'}">
                                        <span class="diet-dot"></span>
                                    </span>
                                    <div class="owner-menu-text">
                                        <div class="owner-menu-title-row">
                                            <strong>${item.name}</strong>
                                            <c:if test="${item.foodCategory != null}">
                                                <span class="cat-tag">${item.foodCategory.name}</span>
                                            </c:if>
                                        </div>
                                        <p class="item-desc">${item.description}</p>
                                    </div>
                                </div>
                                <div class="owner-menu-actions">
                                    <span class="price-tag">₹ ${item.price}</span>
                                    <a href="${pageContext.request.contextPath}/owner/menu-item?id=${item.itemId}"
                                       class="btn-small btn-edit">Edit</a>
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/owner/menu-item/delete"
                                          style="display:inline;"
                                          onsubmit="return confirm('Delete this item?');">
                                        <input type="hidden" name="itemId" value="${item.itemId}">
                                        <button type="submit" class="btn-small btn-delete">Delete</button>
                                    </form>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </c:otherwise>
    </c:choose>
</main>

<jsp:include page="../common/footer.jsp" />