<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quên mật khẩu - Xác thực OTP qua Email</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        .forgot-card {
            max-width: 480px;
            margin: 60px auto;
            border-radius: 16px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
            overflow: hidden;
            background: #ffffff;
        }
        .forgot-header {
            background: #2563eb;
            color: #ffffff;
            padding: 30px 20px;
            text-align: center;
        }
        .forgot-body {
            padding: 30px;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="forgot-card">
        <div class="forgot-header">
            <h3 class="fw-bold mb-1"><i class="bi bi-key-fill me-2"></i>Quên Mật Khẩu</h3>
            <p class="mb-0 text-white-50">Khôi phục tài khoản thông qua mã xác nhận OTP</p>
        </div>

        <div class="forgot-body">
            <!-- Thông báo lỗi nếu có -->
            <c:if test="${alert != null}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>${alert}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <p class="text-muted small mb-4">
                Vui lòng nhập địa chỉ Email hoặc Tên đăng nhập của tài khoản. Chúng tôi sẽ gửi một mã OTP 6 chữ số qua email để bạn thiết lập lại mật khẩu mới.
            </p>

            <form action="${pageContext.request.contextPath}/forgot-password" method="post">
                <div class="mb-4">
                    <label class="form-label fw-semibold text-secondary small">EMAIL HOẶC TÊN ĐĂNG NHẬP:</label>
                    <div class="input-group">
                        <span class="input-group-text bg-light"><i class="bi bi-person-badge"></i></span>
                        <input type="text" class="form-control" name="identifier" 
                               placeholder="Nhập email hoặc username" value="${identifier != null ? identifier : ''}" required autofocus>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold">
                    <i class="bi bi-send me-1"></i>Gửi mã xác nhận OTP
                </button>
            </form>

            <div class="text-center mt-4 pt-3 border-top">
                <a href="${pageContext.request.contextPath}/login" class="text-decoration-none small text-muted">
                    <i class="bi bi-arrow-left me-1"></i>Quay lại trang Đăng nhập
                </a>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap 5 JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
