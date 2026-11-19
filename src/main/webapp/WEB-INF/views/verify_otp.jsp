<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác thực mã OTP - Kích hoạt tài khoản</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        .otp-card {
            max-width: 480px;
            margin: 60px auto;
            border-radius: 16px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
            overflow: hidden;
            background: #ffffff;
        }
        .otp-header {
            background: #2563eb;
            color: #ffffff;
            padding: 30px 20px;
            text-align: center;
        }
        .otp-body {
            padding: 30px;
        }
        .otp-input {
            letter-spacing: 12px;
            font-size: 28px;
            font-weight: 700;
            text-align: center;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="otp-card">
        <div class="otp-header">
            <h3 class="fw-bold mb-1"><i class="bi bi-shield-check me-2"></i>Kích Hoạt Tài Khoản</h3>
            <p class="mb-0 text-white-50">Nhập mã OTP 6 chữ số được gửi qua email</p>
        </div>

        <div class="otp-body">
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
                <i class="bi bi-envelope-check text-primary" style="font-size: 3rem;"></i>
                <p class="text-muted mt-2 mb-0">Mã kích hoạt đã được gửi tới địa chỉ:</p>
                <h6 class="fw-bold text-dark">${email}</h6>
                <small class="text-muted">Mã OTP có hiệu lực trong vòng <strong>5 phút</strong>.</small>
            </div>

            <form action="${pageContext.request.contextPath}/verify-otp" method="post">
                <input type="hidden" name="email" value="${email}">

                <div class="mb-4">
                    <label class="form-label fw-semibold text-secondary small">MÃ XÁC THỰC OTP (6 CHỮ SỐ):</label>
                    <input type="text" class="form-control form-control-lg otp-input" name="otp" 
                           placeholder="------" maxlength="6" pattern="[0-9]{6}" required autofocus>
                </div>

                <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold mb-3">
                    <i class="bi bi-check-circle me-1"></i>Xác nhận & Kích hoạt
                </button>
            </form>

            <div class="d-flex justify-content-between align-items-center mt-3 pt-3 border-top">
                <a href="${pageContext.request.contextPath}/resend-otp?email=${email}" class="text-decoration-none small text-primary fw-semibold">
                    <i class="bi bi-arrow-repeat me-1"></i>Gửi lại mã OTP
                </a>
                <a href="${pageContext.request.contextPath}/login" class="text-decoration-none small text-muted">
                    <i class="bi bi-arrow-left me-1"></i>Quay lại Đăng nhập
                </a>
            </div>

            <!-- Hỗ trợ thử nghiệm tiện lợi (Development Mode Hint) -->
            <c:if test="${sessionScope.devOtp != null}">
                <div class="mt-4 p-3 bg-light rounded border border-warning text-dark small">
                    <div class="fw-bold text-warning-emphasis mb-1">
                        <i class="bi bi-lightbulb-fill me-1"></i>Chế độ thử nghiệm (Dev Mode):
                    </div>
                    <div>Mã OTP vừa được tạo: <strong class="fs-6 text-primary">${sessionScope.devOtp}</strong></div>
                    <div class="text-muted" style="font-size: 11px;">(Đồng thời mã này cũng đã được ghi nhận trong console server và gửi qua SMTP nếu cấu hình)</div>
                </div>
            </c:if>
        </div>
    </div>
</div>

<!-- Bootstrap 5 JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
