<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="${empty item ? 'Add Menu Item' : 'Edit Menu Item'}" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<main class="container narrow">
  <h1 class="page-title">
    ${empty item ? 'Add Menu Item' : 'Edit Menu Item'}
  </h1>

  <c:if test="${not empty error}">
    <div class="alert alert-error">${error}</div>
  </c:if>

  <form method="post" action="${pageContext.request.contextPath}/owner/menu-item" class="owner-form">
    <c:if test="${not empty item}">
      <input type="hidden" name="itemId" value="${item.itemId}">
    </c:if>

    <div class="form-group">
      <label>Item Name *</label>
      <input type="text" name="name" value="${item.name}" required>
    </div>
    <div class="form-group">
      <label>Description</label>
      <textarea name="description" rows="2">${item.description}</textarea>
    </div>

    <div class="form-row">
      <div class="form-group">
        <label>Price (₹) *</label>
        <input type="number" step="0.01" name="price"
               value="${item.price}" min="0" required>
      </div>
      <div class="form-group">
        <label>Food Type *</label>
        <select name="foodType" required>
          <c:forEach var="ft" items="${foodTypes}">
            <option value="${ft}" ${item.foodType == ft ? 'selected' : ''}>${ft}</option>
          </c:forEach>
        </select>
      </div>
    </div>

    <div class="form-group">
      <label>Category</label>
      <select name="foodCategoryId">
        <option value="">— None —</option>
        <c:forEach var="cat" items="${categories}">
          <option value="${cat.categoryId}"
            ${item.foodCategory != null and item.foodCategory.categoryId == cat.categoryId ? 'selected' : ''}>
              ${cat.name}
          </option>
        </c:forEach>
      </select>
    </div>

    <div class="form-group">
      <label>Image URL</label>
      <input type="url" name="imageUrl" value="${item.imageUrl}" placeholder="https://...">
    </div>

    <div class="form-group checkbox-group">
      <label>
        <input type="checkbox" name="isAvailable" value="true"
        ${item == null or item.isAvailable ? 'checked' : ''}>
        Available for ordering
      </label>
    </div>

    <div class="form-actions">
      <button type="submit" class="btn-primary">Save Item</button>
      <a href="${pageContext.request.contextPath}/owner/dashboard"
         class="btn-secondary">Cancel</a>
    </div>
  </form>
</main>

<jsp:include page="../common/footer.jsp" />