<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Trang chủ Người Dùng - JPA 3 Tầng</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body class="bg-light">

<jsp:include page="topbar.jsp" />

<div class="container py-4">
    <!-- Welcome Header -->
    <div class="p-4 p-md-5 mb-4 rounded-3 text-white bg-primary shadow-sm">
        <div class="col-md-9 px-0">
            <h1 class="display-6 fw-bold">Xin chào, ${user.fullName}!</h1>
            <p class="lead my-3">
                Bạn đã đăng nhập thành công vào hệ thống quản lý xác thực bằng <strong>Session</strong> và <strong>JPA (Jakarta Persistence)</strong>.
            </p>
            <div>
                <span class="badge bg-light text-primary fs-6 px-3 py-2 me-2">
                    <i class="bi bi-person-badge me-1"></i>Vai trò: 
                    <c:choose>
                        <c:when test="${user.roleid == 1}">Quản trị viên (Admin)</c:when>
                        <c:when test="${user.roleid == 2}">Quản lý (Manager)</c:when>
                        <c:otherwise>Người dùng (User)</c:otherwise>
                    </c:choose>
                </span>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light btn-sm px-3 py-2">
                    <i class="bi bi-box-arrow-right me-1"></i>Đăng xuất
                </a>
            </div>
        </div>
    </div>

    <div class="row g-4">
        <!-- Thông tin tài khoản người dùng -->
        <div class="col-md-6">
            <div class="card shadow-sm h-100 border-0">
                <div class="card-header bg-white border-bottom py-3">
                    <h5 class="card-title mb-0 text-primary fw-bold">
                        <i class="bi bi-person-lines-fill me-2"></i>Thông Tin Tài Khoản
                    </h5>
                </div>
                <div class="card-body">
                    <table class="table table-hover mb-0">
                        <tbody>
                            <tr>
                                <th scope="row" class="text-secondary" style="width: 35%;">Mã người dùng (ID):</th>
                                <td class="fw-semibold text-dark">${user.id}</td>
                            </tr>
                            <tr>
                                <th scope="row" class="text-secondary">Tên đăng nhập:</th>
                                <td class="fw-semibold text-dark">${user.userName}</td>
                            </tr>
                            <tr>
                                <th scope="row" class="text-secondary">Họ và tên:</th>
                                <td class="fw-semibold text-dark">${user.fullName}</td>
                            </tr>
                            <tr>
                                <th scope="row" class="text-secondary">Email:</th>
                                <td>${user.email}</td>
                            </tr>
                            <tr>
                                <th scope="row" class="text-secondary">Số điện thoại:</th>
                                <td>${user.phone != null && !user.phone.isEmpty() ? user.phone : '<span class="text-muted">(Chưa cập nhật)</span>'}</td>
                            </tr>
                            <tr>
                                <th scope="row" class="text-secondary">Ngày khởi tạo:</th>
                                <td>${user.createdDate}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Thông tin phiên làm việc Session & Cookie -->
        <div class="col-md-6">
            <div class="card shadow-sm h-100 border-0">
                <div class="card-header bg-white border-bottom py-3">
                    <h5 class="card-title mb-0 text-success fw-bold">
                        <i class="bi bi-hdd-network-fill me-2"></i>Thông Tin Phiên Làm Việc (Session & Cookie)
                    </h5>
                </div>
                <div class="card-body">
                    <ul class="list-group list-group-flush">
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <div>
                                <div class="fw-semibold">Session ID</div>
                                <small class="text-muted font-monospace">${pageContext.session.id}</small>
                            </div>
                            <span class="badge bg-success">Đang hoạt động</span>
                        </li>
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <div>
                                <div class="fw-semibold">Thời gian hết hạn phiên (Session Timeout)</div>
                                <small class="text-muted">${pageContext.session.maxInactiveInterval} giây (30 phút)</small>
                            </div>
                            <i class="bi bi-clock-history text-secondary fs-5"></i>
                        </li>
                        <li class="list-group-item d-flex justify-content-between align-items-center">
                            <div>
                                <div class="fw-semibold">Ghi nhớ đăng nhập (Cookie)</div>
                                <small class="text-muted">Tự động nhận diện khi mở lại trình duyệt nếu đã chọn Remember Me</small>
                            </div>
                            <i class="bi bi-cookie text-warning fs-5"></i>
                        </li>
                    </ul>

                    <div class="mt-3 p-3 bg-light rounded border small">
                        <strong>Kiến trúc hệ thống:</strong>
                        <ul class="mb-0 mt-1 ps-3 text-secondary">
                            <li><strong>Presentation:</strong> Servlets (Login, Register, Waiting, Home) + JSP</li>
                            <li><strong>Business Logic:</strong> UserService, VideoService</li>
                            <li><strong>Data Access:</strong> UserDao, VideoDao (JPA Hibernate 6)</li>
                            <li><strong>Quan hệ thực thể:</strong> 1 - N (Users & Videos)</li>
                            <li><strong>Database:</strong> H2 Database Engine</li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Danh sách Video của người dùng (Minh họa mối quan hệ 1 - N) -->
    <div class="card shadow-sm border-0 mt-4">
        <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
            <h5 class="card-title mb-0 fw-bold text-dark">
                <i class="bi bi-collection-play-fill me-2 text-danger"></i>Video Thuộc Sở Hữu Của Tôi (Quan hệ 1 - N)
            </h5>
            <span class="badge bg-primary">${myVideos.size()} videos</span>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${not empty myVideos}">
                    <div class="row g-3">
                        <c:forEach items="${myVideos}" var="vid">
                            <div class="col-md-4">
                                <div class="card h-100 shadow-sm border">
                                    <img src="${vid.poster}" class="card-img-top" alt="${vid.title}" style="height: 160px; object-fit: cover;">
                                    <div class="card-body">
                                        <h6 class="card-title fw-bold text-truncate" title="${vid.title}">${vid.title}</h6>
                                        <p class="card-text text-muted small text-truncate" style="max-height: 40px;">${vid.description}</p>
                                    </div>
                                    <div class="card-footer bg-white border-top-0 d-flex justify-content-between align-items-center small text-muted">
                                        <span><i class="bi bi-eye me-1"></i>${vid.views} lượt xem</span>
                                        <a href="${vid.url}" target="_blank" class="btn btn-sm btn-outline-primary"><i class="bi bi-play-circle me-1"></i>Xem</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="text-center py-4 text-muted">
                        <i class="bi bi-camera-video-off fs-2 d-block mb-2"></i>
                        Bạn chưa có video nào được tải lên trong hệ thống.
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<!-- Bootstrap 5 JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

