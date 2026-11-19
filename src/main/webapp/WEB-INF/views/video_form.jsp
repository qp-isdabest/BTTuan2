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

            <form action="${pageContext.request.contextPath}${isEdit ? '/video/edit' : '/video/add'}" method="post" class="needs-validation" novalidate id="videoForm">
                <c:if test="${isEdit}">
                    <input type="hidden" name="videoId" value="${video.videoId}">
                </c:if>

                <div class="mb-3">
                    <label class="form-label fw-semibold text-secondary small">TIÊU ĐỀ VIDEO *</label>
                    <div class="input-group has-validation">
                        <span class="input-group-text bg-light"><i class="bi bi-fonts"></i></span>
                        <input type="text" class="form-control" name="title" id="videoTitle"
                               value="${video != null ? video.title : ''}" placeholder="Nhập tiêu đề video (3 - 255 ký tự)..." 
                               required minlength="3" maxlength="255" autofocus>
                        <div class="invalid-feedback">
                            Vui lòng nhập tiêu đề video (độ dài từ 3 đến 255 ký tự).
                        </div>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold text-secondary small">MÔ TẢ VIDEO</label>
                    <textarea class="form-control" name="description" rows="3" maxlength="2000"
                              placeholder="Nhập mô tả nội dung video (tối đa 2000 ký tự)...">${video != null ? video.description : ''}</textarea>
                    <div class="form-text small text-muted">Tùy chọn: Tối đa 2000 ký tự.</div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold text-secondary small">ĐƯỜNG DẪN ẢNH THUMBNAIL (POSTER URL)</label>
                    <div class="input-group has-validation">
                        <span class="input-group-text bg-light"><i class="bi bi-image"></i></span>
                        <input type="url" class="form-control" name="poster" id="posterUrl"
                               value="${video != null ? video.poster : ''}" placeholder="https://example.com/image.jpg"
                               pattern="https?://.+">
                        <div class="invalid-feedback">
                            Đường dẫn ảnh phải là URL hợp lệ bắt đầu bằng http:// hoặc https://
                        </div>
                    </div>
                    <div class="form-text small text-muted">Tùy chọn: Nếu để trống hệ thống sẽ dùng ảnh mặc định.</div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold text-secondary small">ĐƯỜNG DẪN VIDEO (URL LINK)</label>
                    <div class="input-group has-validation">
                        <span class="input-group-text bg-light"><i class="bi bi-link-45deg"></i></span>
                        <input type="url" class="form-control" name="url" id="videoUrl"
                               value="${video != null ? video.url : ''}" placeholder="https://www.youtube.com/watch?v=..."
                               pattern="https?://.+">
                        <div class="invalid-feedback">
                            Đường dẫn video phải là URL hợp lệ bắt đầu bằng http:// hoặc https://
                        </div>
                    </div>
                    <div class="form-text small text-muted">Tùy chọn: Link video YouTube, Vimeo hoặc direct video link.</div>
                </div>

                <div class="mb-4 form-check form-switch">
                    <input class="form-check-input" type="checkbox" name="active" id="activeSwitch" 
                           ${video == null || video.active ? 'checked' : ''}>
                    <label class="form-check-label fw-semibold text-secondary small" for="activeSwitch">
                        Kích hoạt hiển thị công khai (Active)
                    </label>
                </div>

                <div class="d-flex justify-content-between align-items-center pt-3 border-top">
                    <a href="${pageContext.request.contextPath}/videos" class="btn btn-outline-secondary">
                        <i class="bi bi-arrow-left me-1"></i>Hủy & Quay lại
                    </a>
                    <button type="submit" class="btn btn-primary px-4 fw-semibold shadow-sm">
                        <i class="bi bi-check2-circle me-1"></i>${isEdit ? 'Lưu Thay Đổi (UPDATE)' : 'Thêm Video Mới (INSERT)'}
                    </button>
                </div>
            </form>

            <script>
                (() => {
                    'use strict';
                    const form = document.getElementById('videoForm');
                    form.addEventListener('submit', event => {
                        if (!form.checkValidity()) {
                            event.preventDefault();
                            event.stopPropagation();
                        }
                        form.classList.add('was-validated');
                    }, false);
                })();
            </script>
        </div>
    </div>
</div>

<!-- Bootstrap 5 JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
