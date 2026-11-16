<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${isEdit ? 'Chỉnh sửa Video' : 'Thêm Video Mới'} - JPA 3 Tầng</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        body {
            background-color: #f8fafc;
        }
        .form-card {
            max-width: 680px;
            margin: 40px auto;
            border-radius: 14px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.08);
            background: #ffffff;
            border: 1px solid #e2e8f0;
        }
        .form-header {
            background: #2563eb;
            color: #ffffff;
            padding: 25px 30px;
            border-top-left-radius: 14px;
            border-top-right-radius: 14px;
        }
        .form-body {
            padding: 30px;
        }
    </style>
</head>
<body>

<jsp:include page="topbar.jsp" />

<div class="container">
    <div class="form-card">
        <div class="form-header">
            <h4 class="fw-bold mb-1">
                <i class="bi ${isEdit ? 'bi-pencil-square' : 'bi-plus-circle'} me-2"></i>
                ${isEdit ? 'Cập Nhật Video (UPDATE)' : 'Thêm Video Mới (CREATE)'}
            </h4>
            <p class="mb-0 text-white-50 small">Quản lý thực thể Video thông qua JPA Hibernate 6</p>
        </div>

        <div class="form-body">
            <!-- Thông báo lỗi nếu có -->
            <c:if test="${alert != null}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>${alert}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}${isEdit ? '/video/edit' : '/video/add'}" method="post">
                <c:if test="${isEdit}">
                    <input type="hidden" name="videoId" value="${video.videoId}">
                </c:if>

                <div class="mb-3">
                    <label class="form-label fw-semibold text-secondary small">TIÊU ĐỀ VIDEO *</label>
                    <div class="input-group">
                        <span class="input-group-text bg-light"><i class="bi bi-fonts"></i></span>
                        <input type="text" class="form-control" name="title" 
                               value="${isEdit ? video.title : ''}" placeholder="Nhập tiêu đề video..." required autofocus>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold text-secondary small">MÔ TẢ VIDEO</label>
                    <textarea class="form-control" name="description" rows="3" 
                              placeholder="Nhập mô tả nội dung video...">${isEdit ? video.description : ''}</textarea>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold text-secondary small">ĐƯỜNG DẪN ẢNH THUMBNAIL (POSTER URL)</label>
                    <div class="input-group">
                        <span class="input-group-text bg-light"><i class="bi bi-image"></i></span>
                        <input type="url" class="form-control" name="poster" 
                               value="${isEdit ? video.poster : ''}" placeholder="https://example.com/image.jpg">
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold text-secondary small">ĐƯỜNG DẪN VIDEO (URL LINK)</label>
                    <div class="input-group">
                        <span class="input-group-text bg-light"><i class="bi bi-link-45deg"></i></span>
                        <input type="url" class="form-control" name="url" 
                               value="${isEdit ? video.url : ''}" placeholder="https://www.youtube.com/watch?v=...">
                    </div>
                </div>

                <div class="mb-4 form-check form-switch">
                    <input class="form-check-input" type="checkbox" name="active" id="activeSwitch" 
                           ${!isEdit || video.active ? 'checked' : ''}>
                    <label class="form-check-label fw-semibold text-secondary small" for="activeSwitch">
                        Kích hoạt hiển thị công khai (Active)
                    </label>
                </div>

                <div class="d-flex justify-content-between align-items-center pt-3 border-top">
                    <a href="${pageContext.request.contextPath}/videos" class="btn btn-outline-secondary">
                        <i class="bi bi-arrow-left me-1"></i>Hủy & Quay lại
                    </a>
                    <button type="submit" class="btn btn-primary px-4 fw-semibold">
                        <i class="bi bi-check2-circle me-1"></i>${isEdit ? 'Lưu Thay Đổi' : 'Thêm Video'}
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- Bootstrap 5 JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
