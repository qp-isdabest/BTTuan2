<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh Sách Videos - Phân Trang 6sp/trang</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Bootstrap Icons -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
</head>
<body class="bg-light">

<jsp:include page="topbar.jsp" />

<div class="container py-4">
    <!-- Header & Action -->
    <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 pb-2 border-bottom gap-2">
        <div>
            <h2 class="fw-bold text-primary mb-1">
                <i class="bi bi-play-btn-fill me-2 text-danger"></i>Danh Sách Videos (Phân Trang 6sp/trang)
            </h2>
            <p class="text-muted mb-0">
                Hiển thị tất cả videos của user với phân trang <strong>6 sản phẩm / trang</strong> trên URL: <code>/video</code>
            </p>
        </div>
        <div>
            <c:if test="${sessionScope.account != null}">
                <a href="${pageContext.request.contextPath}/video/add" class="btn btn-success fw-semibold shadow-sm">
                    <i class="bi bi-plus-circle me-1"></i>Thêm Video Mới
                </a>
            </c:if>
        </div>
    </div>

    <!-- Thông báo kết quả thao tác CRUD -->
    <c:if test="${param.msg == 'add_success'}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="bi bi-check-circle-fill me-2"></i>Thêm video mới thành công!
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${param.msg == 'edit_success'}">
        <div class="alert alert-info alert-dismissible fade show" role="alert">
            <i class="bi bi-info-circle-fill me-2"></i>Cập nhật thông tin video thành công!
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    <c:if test="${param.msg == 'delete_success'}">
        <div class="alert alert-warning alert-dismissible fade show" role="alert">
            <i class="bi bi-trash-fill me-2"></i>Đã xóa video khỏi hệ thống!
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>

    <!-- Tabs chọn phạm vi: Video của tôi (User) & Tất cả Videos -->
    <div class="card shadow-sm border-0 mb-4">
        <div class="card-body p-3">
            <div class="d-flex flex-wrap justify-content-between align-items-center gap-3">
                <ul class="nav nav-pills">
                    <c:if test="${sessionScope.account != null}">
                        <li class="nav-item">
                            <a class="nav-link ${filter == 'my' ? 'active' : ''} fw-semibold" 
                               href="${pageContext.request.contextPath}/video?filter=my">
                                <i class="bi bi-person-video3 me-1"></i>Videos của tôi (${sessionScope.account.fullName})
                                <span class="badge ${filter == 'my' ? 'bg-light text-primary' : 'bg-secondary'} ms-1">${myVideoCount}</span>
                            </a>
                        </li>
                    </c:if>
                    <li class="nav-item">
                        <a class="nav-link ${filter == 'all' ? 'active' : ''} fw-semibold" 
                           href="${pageContext.request.contextPath}/video?filter=all">
                            <i class="bi bi-collection-play me-1"></i>Tất cả Videos hệ thống
                            <span class="badge ${filter == 'all' ? 'bg-light text-primary' : 'bg-secondary'} ms-1">${allVideoCount}</span>
                        </a>
                    </li>
                </ul>

                <!-- Chế độ xem (Card Grid / Bảng) -->
                <div class="btn-group btn-group-sm" role="group">
                    <button type="button" class="btn btn-outline-primary active" id="btnGridView" onclick="switchView('grid')">
                        <i class="bi bi-grid-3x3-gap-fill me-1"></i>Lưới (6sp/trang)
                    </button>
                    <button type="button" class="btn btn-outline-primary" id="btnTableView" onclick="switchView('table')">
                        <i class="bi bi-table me-1"></i>Bảng chi tiết
                    </button>
                </div>
            </div>
        </div>
    </div>

    <!-- Thanh tìm kiếm Video -->
    <div class="card shadow-sm border-0 mb-4">
        <div class="card-body">
            <form action="${pageContext.request.contextPath}/video" method="get" class="row g-2 align-items-center">
                <input type="hidden" name="filter" value="${filter}">
                <c:if test="${not empty targetUserId}">
                    <input type="hidden" name="userId" value="${targetUserId}">
                </c:if>
                <div class="col-md-9 col-sm-8">
                    <div class="input-group">
                        <span class="input-group-text bg-white text-muted"><i class="bi bi-search"></i></span>
                        <input type="text" class="form-control" name="keyword" 
                               placeholder="Tìm kiếm video theo tiêu đề..." value="${keyword != null ? keyword : ''}">
                    </div>
                </div>
                <div class="col-md-3 col-sm-4 d-flex gap-2">
                    <button type="submit" class="btn btn-primary w-100 fw-semibold">
                        <i class="bi bi-search me-1"></i>Tìm kiếm
                    </button>
                    <c:if test="${not empty keyword}">
                        <a href="${pageContext.request.contextPath}/video?filter=${filter}" class="btn btn-outline-secondary">Đặt lại</a>
                    </c:if>
                </div>
            </form>
        </div>
    </div>

    <!-- Thông tin trạng thái phân trang -->
    <div class="d-flex justify-content-between align-items-center mb-3 text-secondary small">
        <div>
            <i class="bi bi-info-circle me-1"></i>
            <c:choose>
                <c:when test="${filter == 'my'}">
                    Đang xem <strong>Videos của người dùng: ${sessionScope.account.fullName}</strong>
                </c:when>
                <c:otherwise>
                    Đang xem <strong>Tất cả Videos trên hệ thống</strong>
                </c:otherwise>
            </c:choose>
            <c:if test="${not empty keyword}">
                (kết quả cho: "<em>${keyword}</em>")
            </c:if>
        </div>
        <div>
            Hiển thị <strong>${videos.size()} / ${totalVideos}</strong> video (Quy chuẩn <strong>6 sản phẩm/trang</strong>)
        </div>
    </div>

    <!-- CHẾ ĐỘ 1: DẠNG LƯỚI CARD (GRID 6 SẢN PHẨM / TRANG) -->
    <div id="gridViewContainer">
        <c:choose>
            <c:when test="${not empty videos}">
                <!-- Lưới 6 thẻ (2 hàng x 3 cột) chuẩn 6 sản phẩm/trang -->
                <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4 mb-4">
                    <c:forEach items="${videos}" var="v" varStatus="loop">
                        <div class="col">
                            <div class="card h-100 shadow-sm border-0 rounded-3 overflow-hidden position-relative">
                                <!-- Badge số thứ tự trong trang -->
                                <span class="position-absolute top-0 start-0 bg-primary text-white px-2 py-1 small rounded-bottom-end fw-bold" style="z-index: 2; font-size: 11px;">
                                    #${(currentPage - 1) * pageSize + loop.index + 1}
                                </span>

                                <!-- Poster Video -->
                                <div class="position-relative bg-dark" style="height: 180px;">
                                    <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}">
                                        <img src="${v.poster}" alt="${v.title}" class="w-100 h-100 object-fit-cover"
                                             onerror="this.src='https://placehold.co/400x225?text=Video+Image'">
                                    </a>
                                    
                                    <!-- Lượt xem -->
                                    <span class="position-absolute bottom-0 end-0 bg-dark bg-opacity-75 text-white px-2 py-1 small rounded-start m-2">
                                        <i class="bi bi-eye me-1"></i>${v.views}
                                    </span>

                                    <!-- Trạng thái -->
                                    <span class="position-absolute top-0 end-0 m-2">
                                        <c:choose>
                                            <c:when test="${v.active}">
                                                <span class="badge bg-success">Hoạt động</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-danger">Khóa</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>

                                <!-- Nội dung Card -->
                                <div class="card-body d-flex flex-column justify-content-between p-3">
                                    <div>
                                        <h5 class="card-title fw-bold text-dark mb-2 text-truncate" title="${v.title}" style="font-size: 16px;">
                                            <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}" class="text-decoration-none text-dark" title="Bấm vào tiêu đề để xem chi tiết và phát video">
                                                ${v.title}
                                            </a>
                                        </h5>
                                        <p class="card-text text-muted small mb-2" style="height: 40px; overflow: hidden; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical;">
                                            ${v.description != null && !v.description.isEmpty() ? v.description : 'Không có mô tả chi tiết.'}
                                        </p>
                                    </div>

                                    <div>
                                        <!-- Thông tin tác giả (Quan hệ 1 - N với User) -->
                                        <div class="d-flex align-items-center justify-content-between pt-2 border-top mb-3">
                                            <span class="badge bg-light text-primary border small">
                                                <i class="bi bi-person-circle me-1"></i>${v.user.fullName}
                                            </span>
                                            <small class="text-muted">Mã: #${v.videoId}</small>
                                        </div>

                                        <!-- Các nút thao tác -->
                                        <div class="d-flex gap-2">
                                            <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}" class="btn btn-primary btn-sm flex-grow-1">
                                                <i class="bi bi-play-circle-fill me-1"></i>Xem & Phát
                                            </a>
                                            <c:if test="${sessionScope.account != null && (sessionScope.account.roleid == 1 || sessionScope.account.id == v.user.id)}">
                                                <a href="${pageContext.request.contextPath}/video/edit?id=${v.videoId}" class="btn btn-outline-warning btn-sm" title="Chỉnh sửa">
                                                    <i class="bi bi-pencil-square"></i>
                                                </a>
                                                <a href="${pageContext.request.contextPath}/video/delete?id=${v.videoId}" 
                                                   class="btn btn-outline-danger btn-sm" title="Xóa"
                                                   onclick="return confirm('Bạn có chắc muốn xóa video: ${v.title}?');">
                                                    <i class="bi bi-trash"></i>
                                                </a>
                                            </c:if>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="card shadow-sm border-0 py-5 text-center text-muted mb-4">
                    <i class="bi bi-camera-video-off fs-1 d-block mb-2 text-secondary"></i>
                    <h5>Không tìm thấy video nào</h5>
                    <p class="small text-muted mb-3">
                        <c:choose>
                            <c:when test="${filter == 'my'}">
                                Bạn chưa đăng tải video nào. Hãy bấm "Thêm Video Mới" để tạo video đầu tiên!
                            </c:when>
                            <c:otherwise>
                                Hệ thống chưa có video nào hoặc không có video phù hợp từ khóa tìm kiếm.
                            </c:otherwise>
                        </c:choose>
                    </p>
                    <c:if test="${sessionScope.account != null}">
                        <div>
                            <a href="${pageContext.request.contextPath}/video/add" class="btn btn-primary btn-sm">
                                <i class="bi bi-plus-circle me-1"></i>Thêm Video Mới
                            </a>
                        </div>
                    </c:if>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- CHẾ ĐỘ 2: DẠNG BẢNG CHI TIẾT (Ẩn mặc định, bật khi người dùng chọn nút Bảng) -->
    <div id="tableViewContainer" style="display: none;">
        <div class="card shadow-sm border-0 mb-4">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead class="table-light">
                        <tr>
                            <th scope="col" class="text-center" style="width: 50px;">#</th>
                            <th scope="col" style="width: 100px;">Poster</th>
                            <th scope="col">Tiêu đề & Mô tả</th>
                            <th scope="col">Người đăng (1-N)</th>
                            <th scope="col" class="text-center">Lượt xem</th>
                            <th scope="col" class="text-center">Trạng thái</th>
                            <th scope="col" class="text-center" style="width: 150px;">Thao tác</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:choose>
                            <c:when test="${not empty videos}">
                                <c:forEach items="${videos}" var="v" varStatus="loop">
                                    <tr>
                                        <td class="text-center fw-bold text-secondary">
                                            ${(currentPage - 1) * pageSize + loop.index + 1}
                                        </td>
                                        <td>
                                            <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}">
                                                <img src="${v.poster}" alt="Poster" class="rounded shadow-sm" 
                                                     style="width: 80px; height: 50px; object-fit: cover;"
                                                     onerror="this.src='https://placehold.co/80x50?text=Video'">
                                            </a>
                                        </td>
                                        <td>
                                            <div class="fw-semibold text-dark">
                                                <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}" class="text-decoration-none text-dark" title="Bấm vào tiêu đề để xem chi tiết và phát video">
                                                    ${v.title}
                                                </a>
                                            </div>
                                            <small class="text-muted text-truncate d-inline-block" style="max-width: 350px;">
                                                ${v.description != null ? v.description : 'Không có mô tả'}
                                            </small>
                                        </td>
                                        <td>
                                            <span class="badge bg-light text-primary border border-primary px-2 py-1">
                                                <i class="bi bi-person-fill me-1"></i>${v.user.fullName}
                                            </span>
                                        </td>
                                        <td class="text-center">
                                            <span class="badge bg-secondary"><i class="bi bi-eye me-1"></i>${v.views}</span>
                                        </td>
                                        <td class="text-center">
                                            <c:choose>
                                                <c:when test="${v.active}">
                                                    <span class="badge bg-success">Hoạt động</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge bg-danger">Khóa</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="text-center">
                                            <div class="btn-group btn-group-sm" role="group">
                                                <a href="${pageContext.request.contextPath}/video/detail?id=${v.videoId}" class="btn btn-outline-primary" title="Xem chi tiết & phát video">
                                                    <i class="bi bi-play-circle-fill"></i>
                                                </a>
                                                <c:if test="${sessionScope.account != null && (sessionScope.account.roleid == 1 || sessionScope.account.id == v.user.id)}">
                                                    <a href="${pageContext.request.contextPath}/video/edit?id=${v.videoId}" class="btn btn-outline-warning" title="Chỉnh sửa">
                                                        <i class="bi bi-pencil-square"></i>
                                                    </a>
                                                    <a href="${pageContext.request.contextPath}/video/delete?id=${v.videoId}" 
                                                       class="btn btn-outline-danger" title="Xóa"
                                                       onclick="return confirm('Bạn có chắc chắn muốn xóa video này không?');">
                                                        <i class="bi bi-trash"></i>
                                                    </a>
                                                </c:if>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="7" class="text-center py-4 text-muted">
                                        Không có dữ liệu video.
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- THANH ĐIỀU HƯỚNG PHÂN TRANG (PAGINATION CONTROLS: 6sp/trang) -->
    <c:if test="${totalPages > 1}">
        <nav aria-label="Page navigation" class="mt-4">
            <ul class="pagination justify-content-center flex-wrap shadow-sm p-2 bg-white rounded">
                <!-- Nút Về Trang Đầu -->
                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                    <a class="page-link" href="${pageContext.request.contextPath}/video?page=1&filter=${filter}&keyword=${keyword != null ? keyword : ''}&userId=${targetUserId != null ? targetUserId : ''}">
                        <i class="bi bi-chevron-double-left"></i> Đầu
                    </a>
                </li>

                <!-- Nút Về Trang Trước -->
                <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                    <a class="page-link" href="${pageContext.request.contextPath}/video?page=${currentPage - 1}&filter=${filter}&keyword=${keyword != null ? keyword : ''}&userId=${targetUserId != null ? targetUserId : ''}">
                        <i class="bi bi-chevron-left"></i> Trước
                    </a>
                </li>

                <!-- Các Số Trang Cụ Thể (1, 2, 3...) -->
                <c:forEach begin="1" end="${totalPages}" var="p">
                    <li class="page-item ${p == currentPage ? 'active' : ''}">
                        <a class="page-link fw-semibold" href="${pageContext.request.contextPath}/video?page=${p}&filter=${filter}&keyword=${keyword != null ? keyword : ''}&userId=${targetUserId != null ? targetUserId : ''}">
                            ${p}
                        </a>
                    </li>
                </c:forEach>

                <!-- Nút Sang Trang Sau -->
                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                    <a class="page-link" href="${pageContext.request.contextPath}/video?page=${currentPage + 1}&filter=${filter}&keyword=${keyword != null ? keyword : ''}&userId=${targetUserId != null ? targetUserId : ''}">
                        Sau <i class="bi bi-chevron-right"></i>
                    </a>
                </li>

                <!-- Nút Đến Trang Cuối -->
                <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                    <a class="page-link" href="${pageContext.request.contextPath}/video?page=${totalPages}&filter=${filter}&keyword=${keyword != null ? keyword : ''}&userId=${targetUserId != null ? targetUserId : ''}">
                        Cuối <i class="bi bi-chevron-double-right"></i>
                    </a>
                </li>
            </ul>
        </nav>
        
        <div class="text-center text-muted small mt-2">
            Trang <strong>${currentPage}</strong> / <strong>${totalPages}</strong> (Mỗi trang hiển thị đúng <strong>6 videos</strong>)
        </div>
    </c:if>
</div>

<!-- JavaScript chuyển đổi chế độ xem Grid / Table -->
<script>
function switchView(mode) {
    const gridContainer = document.getElementById('gridViewContainer');
    const tableContainer = document.getElementById('tableViewContainer');
    const btnGrid = document.getElementById('btnGridView');
    const btnTable = document.getElementById('btnTableView');

    if (mode === 'table') {
        gridContainer.style.display = 'none';
        tableContainer.style.display = 'block';
        btnGrid.classList.remove('active');
        btnTable.classList.add('active');
    } else {
        gridContainer.style.display = 'block';
        tableContainer.style.display = 'none';
        btnTable.classList.remove('active');
        btnGrid.classList.add('active');
    }
}
</script>

<!-- Bootstrap 5 JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
