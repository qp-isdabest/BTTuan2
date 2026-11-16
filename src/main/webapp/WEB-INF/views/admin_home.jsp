<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bảng điều khiển Quản trị (Admin) - JPA 3 Tầng</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body class="bg-light">

<jsp:include page="topbar.jsp" />

<div class="container py-4">
    <!-- Header Admin -->
    <div class="d-flex justify-content-between align-items-center mb-4 pb-2 border-bottom">
        <div>
            <h2 class="fw-bold text-primary mb-1">
                <i class="bi bi-speedometer2 me-2"></i>Bảng Điều Khiển Quản Trị Viên (Admin)
            </h2>
            <p class="text-muted mb-0">Quản lý danh sách người dùng trong hệ thống qua JPA (Jakarta Persistence)</p>
        </div>
        <div>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-primary me-2">
                <i class="bi bi-person-plus me-1"></i>Thêm người dùng
            </a>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-danger">
                <i class="bi bi-box-arrow-right me-1"></i>Đăng xuất
            </a>
        </div>
    </div>

    <!-- Alert / Badge role -->
    <div class="alert alert-info d-flex align-items-center mb-4" role="alert">
        <i class="bi bi-shield-check fs-4 me-3"></i>
        <div>
            Đăng nhập với tư cách: <strong>${adminUser.fullName}</strong> (${adminUser.userName}) - 
            <span class="badge bg-danger">Quyền Quản Trị Hệ Thống (Role 1)</span>
        </div>
    </div>

    <!-- Bảng danh sách người dùng -->
    <div class="card shadow-sm border-0">
        <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
            <h5 class="card-title mb-0 fw-bold text-dark">
                <i class="bi bi-people-fill me-2 text-primary"></i>Danh Sách Tài Khoản Trong Cơ Sở Dữ Liệu
            </h5>
            <span class="badge bg-secondary">Tổng số: ${userList.size()} tài khoản</span>
        </div>
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th scope="col" class="text-center" style="width: 50px;">#ID</th>
                        <th scope="col">Tên đăng nhập</th>
                        <th scope="col">Họ và tên</th>
                        <th scope="col">Email</th>
                        <th scope="col">Số điện thoại</th>
                        <th scope="col" class="text-center">Vai trò (Role)</th>
                        <th scope="col" class="text-center">Trạng thái (OTP)</th>
                        <th scope="col" class="text-center">Ngày tạo</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${userList}" var="u">
                        <tr>
                            <td class="text-center fw-bold text-secondary">${u.id}</td>
                            <td class="fw-semibold text-primary">
                                <i class="bi bi-person-circle me-1 text-muted"></i>${u.userName}
                            </td>
                            <td>${u.fullName}</td>
                            <td>
                                <a href="mailto:${u.email}" class="text-decoration-none">${u.email}</a>
                            </td>
                            <td>${u.phone != null && !u.phone.isEmpty() ? u.phone : '<span class="text-muted">-</span>'}</td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${u.roleid == 1}">
                                        <span class="badge bg-danger px-2 py-1">Admin (1)</span>
                                    </c:when>
                                    <c:when test="${u.roleid == 2}">
                                        <span class="badge bg-warning text-dark px-2 py-1">Manager (2)</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-success px-2 py-1">User (3)</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${u.status == 1}">
                                        <span class="badge bg-success-subtle text-success border border-success px-2 py-1">
                                            <i class="bi bi-check-circle me-1"></i>Đã kích hoạt
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-warning-subtle text-warning-emphasis border border-warning px-2 py-1">
                                            <i class="bi bi-clock-history me-1"></i>Chờ OTP
                                        </span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center text-muted">${u.createdDate}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Bảng danh sách Videos (Quan hệ 1 - N với Users) -->
    <div class="card shadow-sm border-0 mt-5">
        <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
            <h5 class="card-title mb-0 fw-bold text-dark">
                <i class="bi bi-camera-reels-fill me-2 text-danger"></i>Danh Sách Bảng Videos (Mối quan hệ 1 - N với bảng Users)
            </h5>
            <div>
                <span class="badge bg-danger me-2">Tổng số: ${videoList.size()} videos</span>
                <a href="${pageContext.request.contextPath}/video/add" class="btn btn-sm btn-outline-danger fw-semibold">
                    <i class="bi bi-plus-circle me-1"></i>Thêm Video
                </a>
            </div>
        </div>
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th scope="col" class="text-center" style="width: 50px;">#ID</th>
                        <th scope="col" style="width: 100px;">Ảnh Poster</th>
                        <th scope="col">Tiêu đề Video</th>
                        <th scope="col">Người đăng (Tác giả)</th>
                        <th scope="col" class="text-center">Lượt xem</th>
                        <th scope="col" class="text-center">Trạng thái</th>
                        <th scope="col" class="text-center" style="width: 130px;">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${videoList}" var="v">
                        <tr>
                            <td class="text-center fw-bold text-secondary">${v.videoId}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}">
                                    <img src="${v.poster}" alt="Poster" class="rounded" style="width: 80px; height: 50px; object-fit: cover;">
                                </a>
                            </td>
                            <td>
                                <div class="fw-semibold text-dark">
                                    <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}" class="text-decoration-none text-dark" title="Bấm vào tiêu đề để xem chi tiết và phát video">
                                        ${v.title}
                                    </a>
                                </div>
                                <small class="text-muted text-truncate d-inline-block" style="max-width: 380px;">${v.description}</small>
                            </td>
                            <td>
                                <span class="badge bg-primary-subtle text-primary border border-primary px-2 py-1">
                                    <i class="bi bi-person-fill me-1"></i>${v.user.fullName} (${v.user.userName})
                                </span>
                            </td>
                            <td class="text-center">
                                <span class="badge bg-secondary px-2 py-1"><i class="bi bi-eye me-1"></i>${v.views}</span>
                            </td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${v.active}">
                                        <span class="badge bg-success">Hoạt động</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-danger">Khóa</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center">
                                <div class="btn-group btn-group-sm" role="group">
                                    <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}" class="btn btn-outline-primary" title="Xem chi tiết & phát">
                                        <i class="bi bi-play-circle-fill"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/video/edit?id=${v.videoId}" class="btn btn-outline-warning" title="Chỉnh sửa">
                                        <i class="bi bi-pencil-square"></i>
                                    </a>
                                    <a href="${pageContext.request.contextPath}/video/delete?id=${v.videoId}" class="btn btn-outline-danger" title="Xóa"
                                       onclick="return confirm('Bạn có chắc chắn muốn xóa video này không?');">
                                        <i class="bi bi-trash"></i>
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Bootstrap 5 JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

