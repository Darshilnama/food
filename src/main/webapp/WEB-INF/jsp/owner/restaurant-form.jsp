<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="${empty restaurant ? 'Register Restaurant' : 'Edit Restaurant'}" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<main class="container narrow">
    <h1 class="page-title">
        ${empty restaurant ? 'Register Your Restaurant' : 'Edit Restaurant'}
    </h1>

    <c:if test="${not empty error}">
        <div class="alert alert-error">${error}</div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/owner/restaurant" class="owner-form">
        <div class="form-group">
            <label>Restaurant Name *</label>
            <input type="text" name="name" value="${restaurant.name}" required>
        </div>
        <div class="form-group">
            <label>Description</label>
            <textarea name="description" rows="3">${restaurant.description}</textarea>
        </div>
        <div class="form-row">
            <div class="form-group">
                <label>Cuisine Type *</label>
                <input type="text" name="cuisineType" value="${restaurant.cuisineType}"
                       placeholder="e.g. Pizza, Chinese" required>
            </div>
            <div class="form-group">
                <label>Phone</label>
                <input type="tel" name="phone" value="${restaurant.phone}">
            </div>
        </div>
        <div class="form-group">
            <label>Address *</label>
            <input type="text" name="address" value="${restaurant.address}" required>
        </div>
        <div class="form-group">
            <label>Image URL</label>
            <input type="url" name="imageUrl" value="${restaurant.imageUrl}"
                   placeholder="https://...">
        </div>
        <div class="form-row">
            <div class="form-group">
                <label>Avg Delivery Time (minutes)</label>
                <input type="number" name="avgDeliveryTime"
                       value="${restaurant != null ? restaurant.avgDeliveryTime : 30}" min="5">
            </div>
            <div class="form-group">
                <label>Distance from user (km)</label>
                <input type="number" step="0.1" name="distanceKm"
                       value="${restaurant != null ? restaurant.distanceKm : 1.0}" min="0">
            </div>
        </div>
        <div class="form-row">
            <div class="form-group checkbox-group">
                <label>
                    <input type="checkbox" name="vegOnly" value="true"
                    ${restaurant != null and restaurant.vegOnly ? 'checked' : ''}>
                    Pure Veg Restaurant
                </label>
            </div>
            <div class="form-group checkbox-group">
                <label>
                    <input type="checkbox" name="isOpen" value="true"
                    ${restaurant == null or restaurant.isOpen ? 'checked' : ''}>
                    Currently Open
                </label>
            </div>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn-primary">Save Restaurant</button>
            <a href="${pageContext.request.contextPath}/owner/dashboard"
               class="btn-secondary">Cancel</a>
        </div>
    </form>
</main>

<jsp:include page="../common/footer.jsp" />