<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký tài khoản mới - JPA 3 Tầng</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        .register-card {
            max-width: 520px;
            margin: 40px auto;
            border-radius: 16px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
            overflow: hidden;
            background: #ffffff;
        }
        .register-header {
            background: #2563eb;
            color: #ffffff;
            padding: 25px 20px;
            text-align: center;
        }
        .register-body {
            padding: 30px;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="register-card">
        <div class="register-header">
            <h3 class="fw-bold mb-1"><i class="bi bi-person-plus-fill me-2"></i>Tạo Tài Khoản Mới</h3>
            <p class="mb-0 text-white-50">Đăng ký thành viên để trải nghiệm hệ thống</p>
        </div>

        <div class="register-body">
            <!-- Thông báo lỗi nếu có -->
            <c:if test="${alert != null}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>${alert}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/register" method="post" class="needs-validation" novalidate id="registerForm">
                <div class="mb-3">
                    <label class="form-label fw-semibold small text-secondary">Tên đăng nhập *</label>
                    <div class="input-group has-validation">
                        <span class="input-group-text bg-light"><i class="bi bi-person"></i></span>
                        <input type="text" class="form-control" name="username" id="username" placeholder="Nhập username (3-30 ký tự)" 
                               value="${username != null ? username : ''}" required pattern="^[a-zA-Z0-9_]{3,30}$">
                        <div class="invalid-feedback">
                            Tên đăng nhập từ 3 - 30 ký tự, chỉ gồm chữ cái, chữ số và gạch dưới (_).
                        </div>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold small text-secondary">Họ và tên *</label>
                    <div class="input-group has-validation">
                        <span class="input-group-text bg-light"><i class="bi bi-card-text"></i></span>
                        <input type="text" class="form-control" name="fullname" id="fullname" placeholder="Nhập họ và tên đầy đủ" 
                               value="${fullname != null ? fullname : ''}" required minlength="2" maxlength="100">
                        <div class="invalid-feedback">
                            Vui lòng nhập họ và tên (tối thiểu 2 ký tự).
                        </div>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold small text-secondary">Địa chỉ Email *</label>
                    <div class="input-group has-validation">
                        <span class="input-group-text bg-light"><i class="bi bi-envelope"></i></span>
                        <input type="email" class="form-control" name="email" id="email" placeholder="example@domain.com" 
                               value="${email != null ? email : ''}" required>
                        <div class="invalid-feedback">
                            Vui lòng nhập địa chỉ email hợp lệ (ví dụ: user@example.com).
                        </div>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold small text-secondary">Số điện thoại</label>
                    <div class="input-group has-validation">
                        <span class="input-group-text bg-light"><i class="bi bi-telephone"></i></span>
                        <input type="tel" class="form-control" name="phone" id="phone" placeholder="Số điện thoại (10 chữ số)" 
                               value="${phone != null ? phone : ''}" pattern="^(03|05|07|08|09)\d{8}$">
                        <div class="invalid-feedback">
                            Số điện thoại không hợp lệ (cần đúng 10 số bắt đầu bằng 03, 05, 07, 08, 09).
                        </div>
                    </div>
                    <div class="form-text small text-muted">Tùy chọn: Nhập 10 chữ số đầu 03, 05, 07, 08, 09.</div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-semibold small text-secondary">Mật khẩu *</label>
                        <div class="input-group has-validation">
                            <span class="input-group-text bg-light"><i class="bi bi-key"></i></span>
                            <input type="password" class="form-control" name="password" id="regPassword" placeholder="Tối thiểu 6 ký tự" required minlength="6">
                            <div class="invalid-feedback">
                                Mật khẩu phải có ít nhất 6 ký tự.
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-semibold small text-secondary">Xác nhận mật khẩu *</label>
                        <div class="input-group has-validation">
                            <span class="input-group-text bg-light"><i class="bi bi-shield-check"></i></span>
                            <input type="password" class="form-control" name="confirmPassword" id="regConfirmPassword" placeholder="Nhập lại mật khẩu" required minlength="6">
                            <div class="invalid-feedback" id="confirmFeedback">
                                Mật khẩu xác nhận không khớp!
                            </div>
                        </div>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold mt-2 shadow-sm">
                    <i class="bi bi-check2-circle me-1"></i>Tạo tài khoản
                </button>
            </form>

            <script>
                (() => {
                    'use strict';
                    const form = document.getElementById('registerForm');
                    const pwd = document.getElementById('regPassword');
                    const confirmPwd = document.getElementById('regConfirmPassword');

                    function validatePasswordMatch() {
                        if (confirmPwd.value !== pwd.value) {
                            confirmPwd.setCustomValidity('Mật khẩu xác nhận không khớp');
                        } else {
                            confirmPwd.setCustomValidity('');
                        }
                    }

                    pwd.addEventListener('input', validatePasswordMatch);
                    confirmPwd.addEventListener('input', validatePasswordMatch);

                    form.addEventListener('submit', event => {
                        validatePasswordMatch();
                        if (!form.checkValidity()) {
                            event.preventDefault();
                            event.stopPropagation();
                        }
                        form.classList.add('was-validated');
                    }, false);
                })();
            </script>

            <div class="text-center mt-4">
                <p class="mb-0 text-muted">
                    Nếu bạn đã có tài khoản trên hệ thống? 
                    <a href="${pageContext.request.contextPath}/login" class="fw-semibold text-primary text-decoration-none">Đăng nhập</a>
                </p>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap 5 JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

