<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đặt lại mật khẩu mới - Xác thực OTP</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        .reset-card {
            max-width: 500px;
            margin: 50px auto;
            border-radius: 16px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
            overflow: hidden;
            background: #ffffff;
        }
        .reset-header {
            background: #2563eb;
            color: #ffffff;
            padding: 30px 20px;
            text-align: center;
        }
        .reset-body {
            padding: 30px;
        }
        .otp-input {
            letter-spacing: 12px;
            font-size: 26px;
            font-weight: 700;
            text-align: center;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="reset-card">
        <div class="reset-header">
            <h3 class="fw-bold mb-1"><i class="bi bi-shield-lock-fill me-2"></i>Đặt Lại Mật Khẩu</h3>
            <p class="mb-0 text-white-50">Nhập mã OTP và mật khẩu mới cho tài khoản</p>
        </div>

        <div class="reset-body">
            <!-- Thông báo lỗi nếu có -->
            <c:if test="${alert != null}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>${alert}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Thông báo thành công nếu có -->
            <c:if test="${successMsg != null}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="bi bi-check-circle-fill me-2"></i>${successMsg}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <div class="text-center mb-4">
                <i class="bi bi-envelope-paper text-primary" style="font-size: 2.8rem;"></i>
                <p class="text-muted mt-2 mb-0">Mã OTP đã được gửi tới email:</p>
                <h6 class="fw-bold text-dark">${email}</h6>
                <small class="text-muted">Mã có hiệu lực trong vòng <strong>5 phút</strong>.</small>
            </div>

            <form action="${pageContext.request.contextPath}/reset-password" method="post" class="needs-validation" novalidate id="resetForm">
                <input type="hidden" name="email" value="${email}">

                <div class="mb-3">
                    <label class="form-label fw-semibold text-secondary small">MÃ XÁC THỰC OTP (ĐÚNG 6 SỐ) *</label>
                    <div class="has-validation">
                        <input type="text" class="form-control form-control-lg otp-input" name="otp" 
                               placeholder="------" maxlength="6" pattern="[0-9]{6}" required autofocus>
                        <div class="invalid-feedback text-center">
                            Vui lòng nhập đúng 6 chữ số mã OTP.
                        </div>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold text-secondary small">MẬT KHẨU MỚI (TỐI THIỂU 6 KÝ TỰ) *</label>
                    <div class="input-group has-validation">
                        <span class="input-group-text bg-light"><i class="bi bi-key"></i></span>
                        <input type="password" class="form-control" name="newPassword" id="newPassword"
                               placeholder="Nhập mật khẩu mới" minlength="6" required>
                        <div class="invalid-feedback">
                            Mật khẩu mới phải có tối thiểu 6 ký tự.
                        </div>
                    </div>
                </div>

                <div class="mb-4">
                    <label class="form-label fw-semibold text-secondary small">XÁC NHẬN MẬT KHẨU MỚI *</label>
                    <div class="input-group has-validation">
                        <span class="input-group-text bg-light"><i class="bi bi-shield-check"></i></span>
                        <input type="password" class="form-control" name="confirmPassword" id="confirmPassword"
                               placeholder="Nhập lại mật khẩu mới" minlength="6" required>
                        <div class="invalid-feedback" id="resetConfirmFeedback">
                            Mật khẩu xác nhận không khớp!
                        </div>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold shadow-sm">
                    <i class="bi bi-check2-circle me-1"></i>Cập nhật mật khẩu mới
                </button>
            </form>

            <script>
                (() => {
                    'use strict';
                    const form = document.getElementById('resetForm');
                    const newPwd = document.getElementById('newPassword');
                    const confirmPwd = document.getElementById('confirmPassword');

                    function validateMatch() {
                        if (confirmPwd.value !== newPwd.value) {
                            confirmPwd.setCustomValidity('Mật khẩu xác nhận không khớp');
                        } else {
                            confirmPwd.setCustomValidity('');
                        }
                    }

                    newPwd.addEventListener('input', validateMatch);
                    confirmPwd.addEventListener('input', validateMatch);

                    form.addEventListener('submit', event => {
                        validateMatch();
                        if (!form.checkValidity()) {
                            event.preventDefault();
                            event.stopPropagation();
                        }
                        form.classList.add('was-validated');
                    }, false);
                })();
            </script>

            <div class="d-flex justify-content-between align-items-center mt-3 pt-3 border-top">
                <a href="${pageContext.request.contextPath}/resend-forgot-otp?email=${email}" class="text-decoration-none small text-primary fw-semibold">
                    <i class="bi bi-arrow-repeat me-1"></i>Gửi lại mã OTP
                </a>
                <a href="${pageContext.request.contextPath}/login" class="text-decoration-none small text-muted">
                    <i class="bi bi-arrow-left me-1"></i>Quay lại Đăng nhập
                </a>
            </div>

            <!-- Hỗ trợ thử nghiệm tiện lợi (Dev Mode Hint) -->
            <c:if test="${sessionScope.devForgotOtp != null}">
                <div class="mt-4 p-3 bg-light rounded border border-warning text-dark small">
                    <div class="fw-bold text-warning-emphasis mb-1">
                        <i class="bi bi-lightbulb-fill me-1"></i>Chế độ thử nghiệm (Dev Mode):
                    </div>
                    <div>Mã OTP đặt lại mật khẩu: <strong class="fs-6 text-primary">${sessionScope.devForgotOtp}</strong></div>
                    <div class="text-muted" style="font-size: 11px;">(Đồng thời mã này cũng đã được in ra console máy chủ và gửi qua SMTP nếu cấu hình)</div>
                </div>
            </c:if>
        </div>
    </div>
</div>

<!-- Bootstrap 5 JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
