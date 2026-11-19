<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<header>
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm mb-4">
        <div class="container">
            <a class="navbar-brand fw-bold d-flex align-items-center" href="${pageContext.request.contextPath}/waiting">
                <i class="bi bi-shield-lock-fill me-2 fs-4"></i>
                <span>Auth System &amp; Video Manager</span>
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent" aria-controls="navbarContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>

            <div class="collapse navbar-collapse" id="navbarContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/home">
                            <i class="bi bi-house-door me-1"></i>Trang chủ
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/video">
                            <i class="bi bi-camera-reels me-1"></i>Videos (6sp/trang)
                        </a>
                    </li>
                    <c:if test="${sessionScope.account != null && sessionScope.account.roleid == 1}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/admin/home">
                                <i class="bi bi-shield-shaded me-1"></i>Quản trị Admin
                            </a>
                        </li>
                    </c:if>
                </ul>

                <ul class="navbar-nav ms-auto align-items-center">
                    <c:choose>
                        <c:when test="${sessionScope.account == null}">
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/login">
                                    <i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập
                                </a>
                            </li>
                            <li class="nav-item">
                                <a class="btn btn-outline-light btn-sm ms-2 px-3 fw-semibold" href="${pageContext.request.contextPath}/register">
                                    <i class="bi bi-person-plus me-1"></i>Đăng ký
                                </a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle text-white fw-semibold d-flex align-items-center" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                    <c:choose>
                                        <c:when test="${not empty sessionScope.account.avatar}">
                                            <img src="${sessionScope.account.avatar}" alt="Avatar" class="rounded-circle me-2 border border-2 border-white shadow-sm" style="width: 32px; height: 32px; object-fit: cover;">
                                        </c:when>
                                        <c:otherwise>
                                            <i class="bi bi-person-circle fs-5 me-2"></i>
                                        </c:otherwise>
                                    </c:choose>
                                    <span>${sessionScope.account.fullName}</span>
                                    <span class="badge bg-light text-primary ms-2">
                                        <c:choose>
                                            <c:when test="${sessionScope.account.roleid == 1}">Admin</c:when>
                                            <c:when test="${sessionScope.account.roleid == 2}">Manager</c:when>
                                            <c:otherwise>User</c:otherwise>
                                        </c:choose>
                                    </span>
                                </a>
                                <ul class="dropdown-menu dropdown-menu-end shadow">
                                    <li class="dropdown-header text-muted small">Tài khoản: @${sessionScope.account.userName}</li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/profile"><i class="bi bi-person-gear me-2"></i>Hồ sơ tài khoản (Profile)</a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/video"><i class="bi bi-collection-play me-2"></i>Quản lý Videos</a></li>
                                    <c:if test="${sessionScope.account.roleid == 1}">
                                        <li><hr class="dropdown-divider"></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/home"><i class="bi bi-speedometer2 me-2"></i>Bảng điều khiển Admin</a></li>
                                    </c:if>
                                    <li><hr class="dropdown-divider"></li>
                                    <li>
                                        <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">
                                            <i class="bi bi-box-arrow-right me-2"></i>Đăng xuất
                                        </a>
                                    </li>
                                </ul>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>
</header>
