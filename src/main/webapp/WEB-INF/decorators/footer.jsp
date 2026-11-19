<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<footer class="site-footer bg-dark text-white pt-5 pb-4 mt-auto">
    <div class="container">
        <div class="row g-4">
            <!-- Cột 1: Thông tin hệ thống -->
            <div class="col-lg-5 col-md-6">
                <h5 class="fw-bold text-white mb-3 d-flex align-items-center">
                    <i class="bi bi-layers-fill text-primary me-2"></i>Auth &amp; Video System (SiteMesh 3)
                </h5>
                <p class="text-secondary small mb-3">
                    Dự án áp dụng kiến trúc 3 tầng (Controller - Service - DAO), công nghệ Jakarta EE 10 / Servlet 6.0, Hibernate ORM JPA 3.1 và cơ chế trang trí giao diện <strong>SiteMesh Decorator 3</strong> với mẫu giao diện <strong>Bootstrap 5.3.3</strong>.
                </p>
                <div class="d-flex flex-wrap gap-2">
                    <span class="badge bg-primary-subtle text-primary border border-primary-subtle">SiteMesh 3</span>
                    <span class="badge bg-info-subtle text-info border border-info-subtle">Bootstrap 5.3</span>
                    <span class="badge bg-success-subtle text-success border border-success-subtle">Jakarta EE 10</span>
                    <span class="badge bg-warning-subtle text-warning border border-warning-subtle">Hibernate JPA</span>
                </div>
            </div>

            <!-- Cột 2: Thông tin tác giả & bài tập -->
            <div class="col-lg-4 col-md-6">
                <h5 class="fw-bold text-white mb-3">
                    <i class="bi bi-mortarboard-fill text-warning me-2"></i>Thông Tin Sinh Viên
                </h5>
                <ul class="list-unstyled text-secondary small mb-0">
                    <li class="mb-2"><strong class="text-light">Họ và tên:</strong> Phan Thế Quân</li>
                    <li class="mb-2"><strong class="text-light">MSSV:</strong> 24110050</li>
                    <li class="mb-2"><strong class="text-light">Môn học:</strong> Lập trình Web - Học kỳ 1</li>
                    <li class="mb-2"><strong class="text-light">Trường:</strong> ĐH Sư Phạm Kỹ Thuật TP.HCM (HCMUTE)</li>
                    <li><strong class="text-light">Đề tài:</strong> Cấu hình SiteMesh 3 với Template Bootstrap</li>
                </ul>
            </div>

            <!-- Cột 3: Liên kết nhanh -->
            <div class="col-lg-3 col-md-12">
                <h5 class="fw-bold text-white mb-3">
                    <i class="bi bi-link-45deg text-success me-2"></i>Liên Kết Nhanh
                </h5>
                <ul class="list-unstyled small">
                    <li class="mb-2">
                        <a href="${pageContext.request.contextPath}/home" class="text-secondary text-decoration-none">
                            <i class="bi bi-chevron-right me-1"></i>Trang chủ
                        </a>
                    </li>
                    <li class="mb-2">
                        <a href="${pageContext.request.contextPath}/video" class="text-secondary text-decoration-none">
                            <i class="bi bi-chevron-right me-1"></i>Danh sách Videos
                        </a>
                    </li>
                    <c:choose>
                        <c:when test="${sessionScope.account != null}">
                            <li class="mb-2">
                                <a href="${pageContext.request.contextPath}/video/add" class="text-secondary text-decoration-none">
                                    <i class="bi bi-chevron-right me-1"></i>Thêm Video mới
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.request.contextPath}/logout" class="text-danger text-decoration-none">
                                    <i class="bi bi-box-arrow-right me-1"></i>Đăng xuất
                                </a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="mb-2">
                                <a href="${pageContext.request.contextPath}/login" class="text-secondary text-decoration-none">
                                    <i class="bi bi-chevron-right me-1"></i>Đăng nhập
                                </a>
                            </li>
                            <li>
                                <a href="${pageContext.request.contextPath}/register" class="text-secondary text-decoration-none">
                                    <i class="bi bi-chevron-right me-1"></i>Đăng ký tài khoản
                                </a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>

        <hr class="border-secondary my-4 opacity-25">

        <div class="d-flex flex-wrap justify-content-between align-items-center small text-secondary">
            <div>
                &copy; 2026 <strong>24110050_PhanTheQuan_Week2</strong>. Bảo lưu mọi quyền.
            </div>
            <div class="mt-2 mt-md-0">
                <span>Thiết kế giao diện bằng <strong class="text-light">Bootstrap 5</strong> &amp; <strong class="text-light">SiteMesh Decorator 3</strong></span>
            </div>
        </div>
    </div>
</footer>
