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

<jsp:include page="topbar.jsp" />

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

            <form action="${pageContext.request.contextPath}/register" method="post">
                <div class="mb-3">
                    <label class="form-label fw-semibold small text-secondary">Tên đăng nhập *</label>
                    <div class="input-group">
                        <span class="input-group-text bg-light"><i class="bi bi-person"></i></span>
                        <input type="text" class="form-control" name="username" placeholder="Nhập username" 
                               value="${username != null ? username : ''}" required>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold small text-secondary">Họ và tên *</label>
                    <div class="input-group">
                        <span class="input-group-text bg-light"><i class="bi bi-card-text"></i></span>
                        <input type="text" class="form-control" name="fullname" placeholder="Nhập họ và tên đầy đủ" 
                               value="${fullname != null ? fullname : ''}" required>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold small text-secondary">Địa chỉ Email *</label>
                    <div class="input-group">
                        <span class="input-group-text bg-light"><i class="bi bi-envelope"></i></span>
                        <input type="email" class="form-control" name="email" placeholder="example@domain.com" 
                               value="${email != null ? email : ''}" required>
                    </div>
                </div>

                <div class="mb-3">
                    <label class="form-label fw-semibold small text-secondary">Số điện thoại</label>
                    <div class="input-group">
                        <span class="input-group-text bg-light"><i class="bi bi-telephone"></i></span>
                        <input type="tel" class="form-control" name="phone" placeholder="Số điện thoại liên hệ" 
                               value="${phone != null ? phone : ''}">
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-semibold small text-secondary">Mật khẩu *</label>
                        <div class="input-group">
                            <span class="input-group-text bg-light"><i class="bi bi-key"></i></span>
                            <input type="password" class="form-control" name="password" placeholder="Mật khẩu" required>
                        </div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-semibold small text-secondary">Xác nhận mật khẩu *</label>
                        <div class="input-group">
                            <span class="input-group-text bg-light"><i class="bi bi-shield-check"></i></span>
                            <input type="password" class="form-control" name="confirmPassword" placeholder="Nhập lại mật khẩu" required>
                        </div>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary w-100 py-2 fw-semibold mt-2">
                    <i class="bi bi-check2-circle me-1"></i>Tạo tài khoản
                </button>
            </form>

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

