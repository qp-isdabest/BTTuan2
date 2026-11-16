<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Video - CRUD JPA</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body class="bg-light">

<jsp:include page="topbar.jsp" />

<div class="container py-4">
    <!-- Header & Action -->
    <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 pb-2 border-bottom gap-2">
        <div>
            <h2 class="fw-bold text-primary mb-1">
                <i class="bi bi-camera-reels me-2"></i>Quản Lý Video (CRUD JPA)
            </h2>
            <p class="text-muted mb-0">Thực hiện Thêm, Xem, Sửa, Xóa video với mối quan hệ 1 - N với bảng Users</p>
        </div>
        <div>
            <c:if test="${sessionScope.account != null}">
                <a href="${pageContext.request.contextPath}/video/add" class="btn btn-success fw-semibold">
                    <i class="bi bi-plus-circle me-1"></i>Thêm Video Mới
                </a>
            </c:if>
        </div>
    </div>

    <!-- Thông báo kết quả thao tác CRUD -->
    <c:if test="${param.msg == 'add_success'}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i>Thêm video mới thành công!
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${param.msg == 'edit_success'}">
        <div class="alert alert-info alert-dismissible fade show" role="alert">
            <i class="bi bi-info-circle-fill me-2"></i>Cập nhật thông tin video thành công!
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${param.msg == 'delete_success'}">
        <div class="alert alert-warning alert-dismissible fade show" role="alert">
            <i class="bi bi-trash-fill me-2"></i>Đã xóa video khỏi hệ thống!
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Thanh tìm kiếm Video -->
    <div class="card shadow-sm border-0 mb-4">
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/videos" method="get" class="row g-2 align-items-center">
                <div class="col-md-9 col-sm-8">
                    <div class="input-group">
                        <span class="input-group-text bg-white text-muted"><i class="bi bi-search"></i></span>
                        <input type="text" class="form-control" name="keyword" 
                               placeholder="Tìm kiếm video theo tiêu đề..." value="${keyword != null ? keyword : ''}">
                    </div>
                </div>
                <div class="col-md-3 col-sm-4 d-flex gap-2">
                    <button type="submit" class="btn btn-primary w-100 fw-semibold">
                        <i class="bi bi-search me-1"></i>Tìm kiếm
                    </button>
                    <c:if test="${not empty keyword}">
                        <a href="${pageContext.request.contextPath}/videos" class="btn btn-outline-secondary">Đặt lại</a>
                    </c:if>
                </div>
            </form>
        </div>
    </div>

    <!-- Bảng danh sách Video (READ) -->
    <div class="card shadow-sm border-0">
        <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
            <h5 class="card-title mb-0 fw-bold text-dark">
                <i class="bi bi-list-stars me-2 text-primary"></i>Danh Sách Video Trong Hệ Thống
            </h5>
            <span class="badge bg-primary">Tổng số: ${videos.size()} video</span>
        </div>
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-light">
                    <tr>
                        <th scope="col" class="text-center" style="width: 50px;">#ID</th>
                        <th scope="col" style="width: 100px;">Poster</th>
                        <th scope="col">Tiêu đề & Mô tả</th>
                        <th scope="col">Người đăng (1-N)</th>
                        <th scope="col" class="text-center">Lượt xem</th>
                        <th scope="col" class="text-center">Trạng thái</th>
                        <th scope="col" class="text-center" style="width: 160px;">Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty videos}">
                            <c:forEach items="${videos}" var="v">
                                <tr>
                                    <td class="text-center fw-bold text-secondary">${v.videoId}</td>
                                    <td>
                                        <img src="${v.poster}" alt="Poster" class="rounded shadow-sm" 
                                             style="width: 80px; height: 50px; object-fit: cover;"
                                             onerror="this.src='https://placehold.co/80x50?text=Video'">
                                    </td>
                                    <td>
                                        <div class="fw-semibold text-dark">${v.title}</div>
                                        <small class="text-muted text-truncate d-inline-block" style="max-width: 350px;">
                                            ${v.description != null ? v.description : 'Không có mô tả'}
                                        </small>
                                    </td>
                                    <td>
                                        <span class="badge bg-light text-primary border border-primary px-2 py-1">
                                            <i class="bi bi-person-fill me-1"></i>${v.user.fullName}
                                        </span>
                                    </td>
                                    <td class="text-center">
                                        <span class="badge bg-secondary"><i class="bi bi-eye me-1"></i>${v.views}</span>
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
                                            <c:if test="${not empty v.url}">
                                                <a href="${v.url}" target="_blank" class="btn btn-outline-info" title="Xem video">
                                                    <i class="bi bi-play-circle"></i>
                                                </a>
                                            </c:if>
                                            <c:if test="${sessionScope.account != null && (sessionScope.account.roleid == 1 || sessionScope.account.id == v.user.id)}">
                                                <a href="${pageContext.request.contextPath}/video/edit?id=${v.videoId}" class="btn btn-outline-warning" title="Chỉnh sửa">
                                                    <i class="bi bi-pencil-square"></i>
                                                </a>
                                                <a href="${pageContext.request.contextPath}/video/delete?id=${v.videoId}" 
                                                   class="btn btn-outline-danger" title="Xóa"
                                                   onclick="return confirm('Bạn có chắc chắn muốn xóa video này không?');">
                                                    <i class="bi bi-trash"></i>
                                                </a>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="7" class="text-center py-4 text-muted">
                                    <i class="bi bi-inbox fs-3 d-block mb-2"></i>
                                    Không tìm thấy video nào.
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Bootstrap 5 JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
