<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="pageTitle" value="Restaurants" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<main class="container">

  <!-- Search bar -->
  <form action="${pageContext.request.contextPath}/restaurants" method="get" class="search-bar">
    <input type="text" name="search" value="${search}" placeholder="Search restaurants or cuisines...">
    <button type="submit" class="btn-primary">Search</button>
  </form>

  <!-- Filter chips -->
  <div class="filters">
    <span class="filter-label">Filters:</span>

    <a class="chip ${empty rating and empty fast and empty distance ? '' : ''}
                       ${rating == '4.0' ? 'active' : ''}"
       href="${pageContext.request.contextPath}/restaurants?rating=4.0
                  ${not empty cuisine ? '&cuisine='.concat(cuisine) : ''}">⭐ Rating 4.0+</a>

    <a class="chip ${fast == 'true' ? 'active' : ''}"
       href="${pageContext.request.contextPath}/restaurants?fast=true
                  ${not empty cuisine ? '&cuisine='.concat(cuisine) : ''}">⚡ Fast Delivery</a>

    <a class="chip ${distance == '1.0' ? 'active' : ''}"
       href="${pageContext.request.contextPath}/restaurants?distance=1.0
                  ${not empty cuisine ? '&cuisine='.concat(cuisine) : ''}">📍 Near (&lt;1km)</a>

    <a class="chip ${vegOnly ? 'active' : ''}"
       href="${pageContext.request.contextPath}/restaurants?vegOnly=${!vegOnly}
                  ${not empty cuisine ? '&cuisine='.concat(cuisine) : ''}">
      🌱 Veg Only ${vegOnly ? '✓' : ''}
    </a>

    <c:if test="${not empty cuisine or not empty rating or not empty distance or fast == 'true' or vegOnly}">
      <a class="chip clear" href="${pageContext.request.contextPath}/restaurants">✕ Clear All</a>
    </c:if>
  </div>

  <!-- Restaurant grid -->
  <c:choose>
    <c:when test="${empty restaurants}">
      <div class="empty-state">
        <h3>No restaurants found</h3>
        <p>Try adjusting your filters or search terms.</p>
        <a href="${pageContext.request.contextPath}/restaurants" class="btn-secondary">Reset filters</a>
      </div>
    </c:when>
    <c:otherwise>
      <h2 class="section-title">${restaurants.size()} restaurants near you</h2>
      <div class="restaurant-grid">
        <c:forEach var="r" items="${restaurants}">
          <a href="${pageContext.request.contextPath}/restaurant?id=${r.id}" class="restaurant-card">
            <div class="restaurant-img">
              <img src="${r.imageUrl}" alt="${r.name}"
                   onerror="this.src='https://via.placeholder.com/400x200?text=Restaurant'">
              <c:if test="${r.vegOnly}">
                <span class="badge-veg">🌱 Pure Veg</span>
              </c:if>
              <c:if test="${not r.open}">
                <span class="badge-closed">Closed</span>
              </c:if>
            </div>
            <div class="restaurant-info">
              <h3>${r.name}</h3>
              <p class="cuisine">${r.cuisineType}</p>
              <div class="meta">
                <span class="rating">⭐ ${r.rating}</span>
                <span>(${r.totalReviews} reviews)</span>
              </div>
              <div class="meta">
                <span>🕐 ${r.avgDeliveryTime} min</span>
                <span>📍 ${r.distanceKm} km</span>
              </div>
            </div>
          </a>
        </c:forEach>
      </div>
    </c:otherwise>
  </c:choose>

</main>

<jsp:include page="../common/footer.jsp" />