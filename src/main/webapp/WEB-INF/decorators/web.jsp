<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi" class="h-100">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><sitemesh:write property='title'/></title>

    <!-- Bootstrap 5.3.3 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons 1.11.3 -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">

    <!-- Phong cách giao diện chung cho toàn bộ website -->
    <style>
        :root {
            --bs-primary-rgb: 37, 99, 235;
        }
        body {
            display: flex;
            flex-direction: column;
            min-height: 100vh;
            background-color: #f8fafc;
            font-family: system-ui, -apple-system, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
        }
        .main-content {
            flex: 1 0 auto;
        }
        .site-footer {
            flex-shrink: 0;
        }
        .navbar-brand {
            letter-spacing: 0.3px;
        }
        .card {
            border-radius: 12px;
            transition: transform 0.15s ease-in-out, box-shadow 0.15s ease-in-out;
        }
    </style>

    <!-- Nhúng các thẻ head bổ sung từ từng trang JSP con (css riêng, meta riêng, scripts) -->
    <sitemesh:write property='head'/>
</head>
<body class="d-flex flex-column h-100">

    <!-- Header & Navigation Bar (Bootstrap) -->
    <jsp:include page="/WEB-INF/decorators/header.jsp" />

    <!-- Thân trang động nhận nội dung body từ các trang JSP con thông qua SiteMesh 3 -->
    <main class="main-content">
        <sitemesh:write property='body'/>
    </main>

    <!-- Footer dùng chung (Bootstrap) -->
    <jsp:include page="/WEB-INF/decorators/footer.jsp" />

    <!-- Bootstrap 5.3.3 JS Bundle -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
