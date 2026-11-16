<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập hệ thống - JPA 3 Tầng</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
        }
        .login-card {
            max-width: 450px;
            margin: 60px auto;
            border-radius: 16px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
            overflow: hidden;
            background: #ffffff;
        }
        .login-header {
            background: #2563eb;
            color: #ffffff;
            padding: 30px 20px;
            text-align: center;
        }
        .login-body {
            padding: 30px;
        }
        .form-floating > .form-control:focus ~ label {
            color: #2563eb;
        }
    </style>
</head>
<body>

<jsp:include page="topbar.jsp" />

<div class="container">
    <div class="login-card">
        <div class="login-header">
            <h3 class="fw-bold mb-1"><i class="bi bi-person-lock me-2"></i>Đăng Nhập</h3>
            <p class="mb-0 text-white-50">Hệ thống xác thực Session & Cookie (JPA)</p>
        </div>

        <div class="login-body">
            <!-- Thông báo lỗi -->
            <c:if test="${alert != null}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="bi bi-exclamation-triangle-fill me-2"></i>${alert}
                    <c:if test="${pendingEmail != null}">
                        <div class="mt-2">
                            <a href="${pageContext.request.contextPath}/verify-otp?email=${pendingEmail}" class="btn btn-sm btn-outline-danger fw-semibold">
                                <i class="bi bi-shield-check me-1"></i>Nhập mã OTP kích hoạt ngay
                            </a>
                        </div>
                    </c:if>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <!-- Thông báo thành công -->
            <c:if test="${successMsg != null}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="bi bi-check-circle-fill me-2"></i>${successMsg}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <form action="${pageContext.request.contextPath}/login" method="post">
                <div class="input-group mb-3">
                    <span class="input-group-text bg-light"><i class="bi bi-person"></i></span>
                    <input type="text" class="form-control" name="username" placeholder="Tên đăng nhập" 
                           value="${username != null ? username : ''}" required autofocus>
                </div>

                <div class="input-group mb-3">
                    <span class="input-group-text bg-light"><i class="bi bi-key"></i></span>
                    <input type="password" class="form-control" name="password" placeholder="Mật khẩu" required>
                </div>

                <div class="d-flex justify-content-between align-items-center mb-4">
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" name="remember" id="remember">
                        <label class="form-check-label text-muted" for="remember">
                            Ghi nhớ tôi (Lưu Cookie)
                        </label>
                    </div>
                    <a href="#" class="text-decoration-none small text-muted">Quên mật khẩu?</a>
                </div>

                <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold">
                    <i class="bi bi-box-arrow-in-right me-1"></i>Đăng nhập
                </button>
            </form>

            <div class="text-center mt-4">
                <p class="mb-0 text-muted">
                    Chưa có tài khoản? 
                    <a href="${pageContext.request.contextPath}/register" class="fw-semibold text-primary text-decoration-none">Đăng ký ngay</a>
                </p>
            </div>

            <!-- Gợi ý tài khoản demo -->
            <div class="mt-4 p-3 bg-light rounded border small">
                <div class="fw-bold text-secondary mb-1"><i class="bi bi-info-circle me-1"></i>Tài khoản dùng thử:</div>
                <div><strong>Admin:</strong> admin / 123456 (Quyền quản trị)</div>
                <div><strong>User:</strong> user / 123456 (Người dùng)</div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap 5 JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

