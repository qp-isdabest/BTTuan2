<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${video.title} - Phát Video Chi Tiết</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        .video-container {
            position: relative;
            padding-bottom: 56.25%; /* 16:9 Aspect Ratio */
            height: 0;
            overflow: hidden;
            border-radius: 12px;
            background-color: #000;
        }
        .video-container iframe,
        .video-container video {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            border: 0;
        }
        .hover-card:hover {
            background-color: #f8f9fa;
        }
    </style>
</head>
<body class="bg-light">

<jsp:include page="topbar.jsp" />

<div class="container py-4">
    <!-- Breadcrumb điều hướng -->
    <nav aria-label="breadcrumb" class="mb-3">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home" class="text-decoration-none">Trang chủ</a></li>
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/video" class="text-decoration-none">Videos</a></li>
            <li class="breadcrumb-item active text-truncate" style="max-width: 400px;" aria-current="page">${video.title}</li>
        </ol>
    </nav>

    <div class="row g-4">
        <!-- CỘT TRÁI: KHUNG PHÁT VIDEO VÀ CHI TIẾT VIDEO (col-lg-8) -->
        <div class="col-lg-8">
            <!-- 1. KHUNG PHÁT VIDEO (VIDEO PLAYER) -->
            <div class="video-container shadow-sm mb-3">
                <c:choose>
                    <c:when test="${video.isYouTube()}">
                        <iframe src="${video.embedUrl}" 
                                title="${video.title}" 
                                allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" 
                                allowfullscreen>
                        </iframe>
                    </c:when>
                    <c:when test="${not empty video.url}">
                        <video controls autoplay class="w-100 h-100">
                            <source src="${video.url}" type="video/mp4">
                            Trình duyệt của bạn không hỗ trợ phát trực tiếp video này.
                        </video>
                    </c:when>
                    <c:otherwise>
                        <div class="d-flex flex-column align-items-center justify-content-center h-100 text-white">
                            <i class="bi bi-camera-video-off fs-1 mb-2"></i>
                            <p>Không có đường dẫn phát video</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- 2. THÔNG TIN CHI TIẾT VIDEO -->
            <div class="card shadow-sm border-0 mb-4">
                <div class="card-body p-4">
                    <!-- Tiêu đề Video -->
                    <h2 class="card-title fw-bold text-dark mb-3" style="font-size: 22px;">
                        ${video.title}
                    </h2>

                    <!-- Hàng thông số & Lượt xem -->
                    <div class="d-flex flex-wrap justify-content-between align-items-center pb-3 border-bottom gap-2">
                        <div class="d-flex align-items-center gap-2">
                            <span class="badge bg-danger fs-6 px-3 py-2">
                                <i class="bi bi-eye-fill me-1"></i>${video.views} lượt xem
                            </span>
                            <c:choose>
                                <c:when test="${video.active}">
                                    <span class="badge bg-success fs-6 px-3 py-2">
                                        <i class="bi bi-check-circle-fill me-1"></i>Đang hoạt động
                                    </span>
                                </c:when>
                                <c:otherwise>
                                    <span class="badge bg-secondary fs-6 px-3 py-2">
                                        <i class="bi bi-lock-fill me-1"></i>Đã khóa
                                    </span>
                                </c:otherwise>
                            </c:choose>
                        </div>

                        <!-- Thao tác quản trị (Sửa / Xóa) nếu là tác giả hoặc Admin -->
                        <div class="d-flex gap-2">
                            <c:if test="${not empty video.url}">
                                <a href="${video.url}" target="_blank" class="btn btn-outline-danger btn-sm">
                                    <i class="bi bi-youtube me-1"></i>Xem gốc
                                </a>
                            </c:if>
                            <c:if test="${sessionScope.account != null && (sessionScope.account.roleid == 1 || sessionScope.account.id == video.user.id)}">
                                <a href="${pageContext.request.contextPath}/video/edit?id=${video.videoId}" class="btn btn-outline-warning btn-sm">
                                    <i class="bi bi-pencil-square me-1"></i>Sửa
                                </a>
                                <a href="${pageContext.request.contextPath}/video/delete?id=${video.videoId}" 
                                   class="btn btn-outline-danger btn-sm"
                                   onclick="return confirm('Bạn có chắc muốn xóa video này không?');">
                                    <i class="bi bi-trash me-1"></i>Xóa
                                </a>
                            </c:if>
                        </div>
                    </div>

                    <!-- Thông tin Tác giả (Quan hệ 1 - N với User) -->
                    <div class="d-flex align-items-center my-3 p-3 bg-light rounded-3">
                        <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-3" 
                             style="width: 48px; height: 48px; font-size: 20px;">
                            <i class="bi bi-person-fill"></i>
                        </div>
                        <div class="flex-grow-1">
                            <div class="fw-bold text-dark">${video.user.fullName}</div>
                            <small class="text-muted d-block">
                                <i class="bi bi-envelope me-1"></i>${video.user.email} &bull; 
                                Tên đăng nhập: <strong>${video.user.userName}</strong>
                            </small>
                        </div>
                        <div>
                            <span class="badge bg-primary px-3 py-2">
                                <c:choose>
                                    <c:when test="${video.user.roleid == 1}">Quản trị viên (Admin)</c:when>
                                    <c:when test="${video.user.roleid == 2}">Quản lý (Manager)</c:when>
                                    <c:otherwise>Người dùng (User)</c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                    </div>

                    <!-- Mô tả chi tiết video -->
                    <div class="mt-3">
                        <h6 class="fw-bold text-secondary mb-2">
                            <i class="bi bi-card-text me-1"></i>Mô tả chi tiết nội dung:
                        </h6>
                        <div class="p-3 bg-white rounded border text-secondary" style="line-height: 1.7;">
                            <c:choose>
                                <c:when test="${not empty video.description}">
                                    <p class="mb-0" style="white-space: pre-line;">${video.description}</p>
                                </c:when>
                                <c:otherwise>
                                    <p class="mb-0 fst-italic text-muted">Chưa có mô tả chi tiết cho video này.</p>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <!-- Nút quay lại -->
                    <div class="mt-4 pt-3 border-top d-flex gap-2">
                        <a href="${pageContext.request.contextPath}/video" class="btn btn-outline-primary">
                            <i class="bi bi-arrow-left me-1"></i>Quay lại danh sách Videos
                        </a>
                        <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary">
                            <i class="bi bi-house me-1"></i>Về Trang chủ
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- CỘT PHẢI: DANH SÁCH VIDEO LIÊN QUAN / MỚI NHẤT (col-lg-4) -->
        <div class="col-lg-4">
            <div class="card shadow-sm border-0 sticky-top" style="top: 20px;">
                <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                    <h5 class="card-title mb-0 fw-bold text-dark">
                        <i class="bi bi-fire me-2 text-danger"></i>Video Mới Nhất
                    </h5>
                    <span class="badge bg-danger">${relatedVideos.size()} video</span>
                </div>
                <div class="list-group list-group-flush" style="max-height: 700px; overflow-y: auto;">
                    <c:forEach items="${relatedVideos}" var="rv">
                        <a href="${pageContext.request.contextPath}/video/detail?id=${rv.videoId}" 
                           class="list-group-item list-group-item-action p-2 d-flex gap-2 align-items-center hover-card ${rv.videoId == video.videoId ? 'bg-light border-start border-4 border-primary' : ''}">
                            <div class="position-relative flex-shrink-0" style="width: 110px; height: 68px;">
                                <img src="${rv.poster}" alt="${rv.title}" class="w-100 h-100 object-fit-cover rounded"
                                     onerror="this.src='https://placehold.co/120x70?text=Video'">
                                <span class="position-absolute bottom-0 end-0 bg-dark bg-opacity-75 text-white px-1 small rounded-start m-1" style="font-size: 10px;">
                                    <i class="bi bi-eye"></i> ${rv.views}
                                </span>
                            </div>
                            <div class="flex-grow-1 overflow-hidden">
                                <h6 class="mb-1 text-dark text-truncate fw-semibold" style="font-size: 13px;" title="${rv.title}">
                                    ${rv.title}
                                </h6>
                                <small class="text-muted d-block text-truncate" style="font-size: 11px;">
                                    <i class="bi bi-person me-1"></i>${rv.user.fullName}
                                </small>
                                <c:if test="${rv.videoId == video.videoId}">
                                    <span class="badge bg-primary text-white mt-1" style="font-size: 10px;">
                                        <i class="bi bi-play-circle-fill me-1"></i>Đang phát
                                    </span>
                                </c:if>
                            </div>
                        </a>
                    </c:forEach>
                </div>
                <div class="card-footer bg-white text-center py-2">
                    <a href="${pageContext.request.contextPath}/video" class="small text-decoration-none fw-semibold">
                        Xem tất cả video (6sp/trang) <i class="bi bi-chevron-right"></i>
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap 5 JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
