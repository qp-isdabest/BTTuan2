<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ Sơ Cá Nhân - Cập Nhật Profile &amp; Cloudinary</title>
    <style>
        .profile-avatar-container {
            position: relative;
            width: 140px;
            height: 140px;
            margin: 0 auto 20px auto;
        }
        .profile-avatar {
            width: 140px;
            height: 140px;
            object-fit: cover;
            border-radius: 50%;
            border: 4px solid #ffffff;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.15);
        }
        .avatar-preview-badge {
            position: absolute;
            bottom: 0;
            right: 5px;
            background: #2563eb;
            color: white;
            border-radius: 50%;
            width: 36px;
            height: 36px;
            display: flex;
            align-items: center;
            justify-content: center;
            border: 2px solid white;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.2);
        }
    </style>
</head>
<body class="bg-light">

<div class="container py-4">
    <!-- Breadcrumb điều hướng -->
    <nav aria-label="breadcrumb" class="mb-4">
        <ol class="breadcrumb">
            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/home" class="text-decoration-none"><i class="bi bi-house-door me-1"></i>Trang chủ</a></li>
            <li class="breadcrumb-item active" aria-current="page">Hồ sơ người dùng</li>
        </ol>
    </nav>

    <!-- Thông báo kết quả thao tác -->
    <c:if test="${param.msg == 'update_success'}">
        <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
            <i class="bi bi-check-circle-fill me-2 fs-5"></i>
            <strong>Thành công!</strong> Thông tin hồ sơ cá nhân đã được cập nhật thành công vào hệ thống.
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <c:if test="${param.msg == 'email_update_success'}">
        <div class="alert alert-success alert-dismissible fade show shadow-sm" role="alert">
            <i class="bi bi-shield-fill-check me-2 fs-5"></i>
            <strong>Hoàn tất!</strong> Mã OTP đã được xác thực thành công. Địa chỉ email mới đã được cập nhật chính thức!
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Thông báo lỗi nếu có -->
    <c:if test="${alert != null}">
        <div class="alert alert-danger alert-dismissible fade show shadow-sm" role="alert">
            <i class="bi bi-exclamation-triangle-fill me-2 fs-5"></i>
            <strong>Đã xảy ra lỗi:</strong> ${alert}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <div class="row g-4">
        <!-- Cột trái: Thẻ hiển thị tóm tắt thông tin tài khoản -->
        <div class="col-lg-4">
            <div class="card shadow-sm border-0 text-center p-4">
                <div class="profile-avatar-container">
                    <c:choose>
                        <c:when test="${not empty user.avatar}">
                            <img src="${user.avatar}" alt="${user.fullName}" class="profile-avatar" id="avatarPreview">
                        </c:when>
                        <c:otherwise>
                            <img src="https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=300" alt="${user.fullName}" class="profile-avatar" id="avatarPreview">
                        </c:otherwise>
                    </c:choose>
                    <div class="avatar-preview-badge" title="Tải ảnh để cập nhật">
                        <i class="bi bi-camera-fill"></i>
                    </div>
                </div>

                <h4 class="fw-bold text-dark mb-1">${user.fullName}</h4>
                <p class="text-muted small mb-2">@${user.userName}</p>

                <div class="mb-3">
                    <c:choose>
                        <c:when test="${user.roleid == 1}">
                            <span class="badge bg-danger fs-6 px-3 py-1"><i class="bi bi-shield-shaded me-1"></i>Quản trị viên (Admin)</span>
                        </c:when>
                        <c:when test="${user.roleid == 2}">
                            <span class="badge bg-warning text-dark fs-6 px-3 py-1"><i class="bi bi-briefcase me-1"></i>Quản lý (Manager)</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge bg-primary fs-6 px-3 py-1"><i class="bi bi-person me-1"></i>Người dùng (User)</span>
                        </c:otherwise>
                    </c:choose>
                </div>

                <hr class="my-3 text-muted">

                <ul class="list-group list-group-flush text-start small">
                    <li class="list-group-item d-flex justify-content-between px-0">
                        <span class="text-secondary"><i class="bi bi-envelope me-2"></i>Email:</span>
                        <strong class="text-truncate" style="max-width: 180px;">${user.email}</strong>
                    </li>
                    <li class="list-group-item d-flex justify-content-between px-0">
                        <span class="text-secondary"><i class="bi bi-telephone me-2"></i>Điện thoại:</span>
                        <strong>${not empty user.phone ? user.phone : 'Chưa cập nhật'}</strong>
                    </li>
                    <li class="list-group-item d-flex justify-content-between px-0">
                        <span class="text-secondary"><i class="bi bi-calendar-check me-2"></i>Ngày tạo:</span>
                        <span>
                            <c:choose>
                                <c:when test="${not empty user.createdDate}">
                                    <fmt:formatDate value="${user.createdDate}" pattern="dd/MM/yyyy" />
                                </c:when>
                                <c:otherwise>Hôm nay</c:otherwise>
                            </c:choose>
                        </span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between px-0">
                        <span class="text-secondary"><i class="bi bi-cloud-arrow-up me-2"></i>Lưu trữ Avatar:</span>
                        <span class="badge bg-info-subtle text-info border border-info-subtle">Cloudinary CDN</span>
                    </li>
                </ul>

                <div class="mt-4 pt-2">
                    <a href="${pageContext.request.contextPath}/video" class="btn btn-outline-primary w-100 fw-semibold">
                        <i class="bi bi-camera-reels me-1"></i>Quản lý danh sách Videos
                    </a>
                </div>
            </div>
        </div>

        <!-- Cột phải: Form cập nhật thông tin & Upload ảnh lên Cloudinary -->
        <div class="col-lg-8">
            <div class="card shadow-sm border-0">
                <div class="card-header bg-white border-bottom py-3">
                    <h5 class="card-title mb-0 fw-bold text-primary">
                        <i class="bi bi-person-gear me-2"></i>Cập Nhật Thông Tin Cá Nhân (Profile)
                    </h5>
                </div>
                <div class="card-body p-4">
                    <form action="${pageContext.request.contextPath}/profile" method="post" 
                          enctype="multipart/form-data" class="needs-validation" novalidate id="profileForm">

                        <!-- Tên đăng nhập (chỉ đọc) -->
                        <div class="mb-3">
                            <label class="form-label fw-semibold text-secondary small">TÊN ĐĂNG NHẬP (USERNAME)</label>
                            <div class="input-group">
                                <span class="input-group-text bg-light"><i class="bi bi-lock-fill text-muted"></i></span>
                                <input type="text" class="form-control bg-light" value="${user.userName}" readonly disabled>
                            </div>
                            <div class="form-text small text-muted">Tên đăng nhập là định danh cố định và không thể thay đổi.</div>
                        </div>

                        <!-- Họ và tên -->
                        <div class="mb-3">
                            <label class="form-label fw-semibold text-secondary small">HỌ VÀ TÊN *</label>
                            <div class="input-group has-validation">
                                <span class="input-group-text bg-light"><i class="bi bi-card-text"></i></span>
                                <input type="text" class="form-control" name="fullname" id="fullname" 
                                       value="${not empty inputFullname ? inputFullname : user.fullName}" 
                                       placeholder="Nhập họ và tên đầy đủ" required minlength="2" maxlength="100">
                                <div class="invalid-feedback">
                                    Vui lòng nhập họ và tên (tối thiểu 2 ký tự, tối đa 100 ký tự).
                                </div>
                            </div>
                        </div>

                        <!-- Địa chỉ Email -->
                        <div class="mb-3">
                            <label class="form-label fw-semibold text-secondary small">ĐỊA CHỈ EMAIL *</label>
                            <div class="input-group has-validation">
                                <span class="input-group-text bg-light"><i class="bi bi-envelope"></i></span>
                                <input type="email" class="form-control" name="email" id="email" 
                                       value="${not empty inputEmail ? inputEmail : user.email}" 
                                       placeholder="example@domain.com" required>
                                <div class="invalid-feedback">
                                    Vui lòng nhập địa chỉ email hợp lệ.
                                </div>
                            </div>
                            <div class="alert alert-info py-2 px-3 mt-2 small d-flex align-items-center mb-0" role="alert">
                                <i class="bi bi-info-circle-fill me-2 fs-5"></i>
                                <div>
                                    <strong>Lưu ý bảo mật:</strong> Nếu bạn đổi sang địa chỉ Email mới, hệ thống sẽ tự động gửi mã <strong>OTP 6 số</strong> đến email mới để bạn xác thực trước khi cập nhật chính thức.
                                </div>
                            </div>
                        </div>

                        <!-- Số điện thoại -->
                        <div class="mb-3">
                            <label class="form-label fw-semibold text-secondary small">SỐ ĐIỆN THOẠI</label>
                            <div class="input-group has-validation">
                                <span class="input-group-text bg-light"><i class="bi bi-telephone"></i></span>
                                <input type="tel" class="form-control" name="phone" id="phone" 
                                       value="${not empty inputPhone ? inputPhone : user.phone}" 
                                       placeholder="Nhập số điện thoại (10 chữ số)" pattern="^(03|05|07|08|09)\d{8}$">
                                <div class="invalid-feedback">
                                    Số điện thoại phải gồm 10 số và bắt đầu bằng 03, 05, 07, 08, 09.
                                </div>
                            </div>
                            <div class="form-text small text-muted">Tùy chọn: Nhập 10 chữ số đầu số di động Việt Nam.</div>
                        </div>

                        <!-- Upload Avatar lên Cloudinary bằng Multipart -->
                        <div class="mb-4">
                            <label class="form-label fw-semibold text-secondary small">ẢNH ĐẠI DIỆN MỚI (UPLOAD CLOUDINARY MULTIPART)</label>
                            <div class="input-group has-validation">
                                <span class="input-group-text bg-light"><i class="bi bi-cloud-arrow-up"></i></span>
                                <input type="file" class="form-control" name="image" id="avatarInput" accept="image/*">
                            </div>
                            <div class="form-text small text-muted">
                                Chọn file ảnh (JPG, PNG, WEBP, GIF). File sẽ được upload trực tiếp lên <strong>Cloudinary</strong> và liên kết với tài khoản của bạn.
                            </div>
                        </div>

                        <div class="d-flex justify-content-between align-items-center pt-3 border-top">
                            <a href="${pageContext.request.contextPath}/home" class="btn btn-outline-secondary">
                                <i class="bi bi-arrow-left me-1"></i>Quay lại Trang chủ
                            </a>
                            <button type="submit" class="btn btn-primary px-4 fw-semibold shadow-sm">
                                <i class="bi bi-check2-circle me-1"></i>Lưu Thay Đổi Hồ Sơ
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- JavaScript xem trước ảnh đại diện khi chọn file & Validation Form -->
<script>
    (() => {
        'use strict';
        // 1. Preview ảnh avatar khi người dùng chọn file
        const avatarInput = document.getElementById('avatarInput');
        const avatarPreview = document.getElementById('avatarPreview');

        if (avatarInput && avatarPreview) {
            avatarInput.addEventListener('change', function(e) {
                const file = e.target.files[0];
                if (file) {
                    if (!file.type.startsWith('image/')) {
                        alert('Vui lòng chọn đúng tệp hình ảnh (JPG, PNG, WEBP, GIF)!');
                        avatarInput.value = '';
                        return;
                    }
                    const reader = new FileReader();
                    reader.onload = function(event) {
                        avatarPreview.src = event.target.result;
                    };
                    reader.readAsDataURL(file);
                }
            });
        }

        // 2. Client-side Validation Bootstrap
        const form = document.getElementById('profileForm');
        if (form) {
            form.addEventListener('submit', event => {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        }
    })();
</script>

</body>
</html>
