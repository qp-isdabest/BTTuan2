<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<nav class="navbar navbar-expand-lg navbar-dark bg-primary shadow-sm mb-4">
    <div class="container">
        <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/waiting">
            <i class="bi bi-shield-lock-fill me-2"></i>Auth System (JPA 3-Tier)
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="navbarContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/home">Trang chủ</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/videos">
                        <i class="bi bi-camera-reels me-1"></i>Quản lý Videos
                    </a>
                </li>
                <c:if test="${sessionScope.account != null && sessionScope.account.roleid == 1}">
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/admin/home">Quản trị Admin</a>
                    </li>
                </c:if>
            </ul>

            <ul class="navbar-nav ms-auto">
                <c:choose>
                    <c:when test="${sessionScope.account == null}">
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/login">
                                <i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="${pageContext.request.contextPath}/register">
                                <i class="bi bi-person-plus me-1"></i>Đăng ký
                            </a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle text-white fw-semibold" href="#" role="button" data-bs-toggle="dropdown">
                                <i class="bi bi-person-circle me-1"></i>${sessionScope.account.fullName}
                                <span class="badge bg-light text-primary ms-1">
                                    <c:choose>
                                        <c:when test="${sessionScope.account.roleid == 1}">Admin</c:when>
                                        <c:when test="${sessionScope.account.roleid == 2}">Manager</c:when>
                                        <c:otherwise>User</c:otherwise>
                                    </c:choose>
                                </span>
                            </a>
                            <ul class="dropdown-menu dropdown-menu-end shadow">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/home"><i class="bi bi-person me-2"></i>Thông tin tài khoản</a></li>
                                <c:if test="${sessionScope.account.roleid == 1}">
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

