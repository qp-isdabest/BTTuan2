<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác Thực Email Mới - OTP Email</title>
    <style>
        .otp-card {
            max-width: 500px;
            margin: 50px auto;
            border-radius: 16px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            background: #ffffff;
            border: 1px solid #e2e8f0;
        }
        .otp-header {
            background: #2563eb;
            color: #ffffff;
            padding: 30px 20px;
            text-align: center;
        }
        .otp-body {
            padding: 35px 30px;
        }
        .otp-input {
            letter-spacing: 12px;
            font-size: 28px;
            text-align: center;
            font-weight: 700;
        }
    </style>
</head>
<body class="bg-light">

<div class="container py-4">
    <div class="otp-card">
        <div class="otp-header">
            <h3 class="fw-bold mb-1"><i class="bi bi-shield-check me-2"></i>Xác Thực Email Mới</h3>
            <p class="mb-0 text-white-50">Nhập mã OTP để xác nhận thay đổi địa chỉ email</p>
        </div>

        <div class="otp-body">
            <!-- Thông báo lỗi nếu có -->
            <c:if test="${alert != null}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>${alert}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Thông báo gửi lại OTP thành công -->
            <c:if test="${param.msg == 'resent'}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="bi bi-check-circle-fill me-2"></i>Mã OTP xác thực mới đã được gửi tới email của bạn!
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <div class="text-center mb-4">
                <i class="bi bi-envelope-at text-primary" style="font-size: 3rem;"></i>
                <p class="text-muted mt-2 mb-1">Mã xác nhận OTP 6 số đã được gửi tới Email mới:</p>
                <h5 class="fw-bold text-dark text-break">${newEmail}</h5>
                <small class="text-muted">Mã OTP có hiệu lực trong vòng <strong>5 phút</strong>.</small>
            </div>

            <form action="${pageContext.request.contextPath}/verify-email-otp" method="post" class="needs-validation" novalidate id="otpForm">
                <div class="mb-4">
                    <label class="form-label fw-semibold text-secondary small">MÃ XÁC THỰC OTP (ĐÚNG 6 CHỮ SỐ) *</label>
                    <div class="has-validation">
                        <input type="text" class="form-control form-control-lg otp-input" name="otp" 
                               placeholder="------" maxlength="6" pattern="[0-9]{6}" required autofocus>
                        <div class="invalid-feedback text-center">
                            Vui lòng nhập đúng 6 chữ số mã OTP xác nhận.
                        </div>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold mb-3 shadow-sm">
                    <i class="bi bi-check-circle me-1"></i>Xác nhận & Cập nhật Email
                </button>
            </form>

            <div class="d-flex justify-content-between align-items-center mt-3 pt-3 border-top">
                <a href="${pageContext.request.contextPath}/resend-email-otp" class="text-decoration-none small text-primary fw-semibold">
                    <i class="bi bi-arrow-repeat me-1"></i>Gửi lại mã OTP
                </a>
                <a href="${pageContext.request.contextPath}/profile" class="text-decoration-none small text-muted">
                    <i class="bi bi-arrow-left me-1"></i>Hủy bỏ & Quay lại
                </a>
            </div>

            <!-- Hỗ trợ thử nghiệm tiện lợi (Development Mode Hint) -->
            <c:if test="${sessionScope.dev_email_otp != null}">
                <div class="alert alert-warning mt-4 mb-0 small" role="alert">
                    <div class="fw-bold"><i class="bi bi-code-slash me-1"></i>Hỗ trợ chế độ thử nghiệm nội bộ:</div>
                    Mã OTP vừa tạo: <span class="badge bg-dark fs-6">${sessionScope.dev_email_otp}</span>
                    <div class="text-muted" style="font-size: 11px;">(Được hiển thị để tiện kiểm thử khi chưa kết nối SMTP Gmail thật)</div>
                </div>
            </c:if>
        </div>
    </div>
</div>

<script>
    (() => {
        'use strict';
        const form = document.getElementById('otpForm');
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
