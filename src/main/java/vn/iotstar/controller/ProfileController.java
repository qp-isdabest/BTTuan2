package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.CloudinaryUtil;
import vn.iotstar.util.Constant;
import vn.iotstar.util.EmailUtil;
import vn.iotstar.util.ValidationUtil;

import java.io.IOException;
import java.util.Random;

@WebServlet(urlPatterns = {"/profile", "/verify-email-otp", "/resend-email-otp"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class ProfileController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(Constant.SESSION_ACCOUNT) == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String path = req.getServletPath();

        switch (path) {
            case "/verify-email-otp":
                showVerifyEmailOtp(req, resp, session);
                break;
            case "/resend-email-otp":
                processResendEmailOtp(req, resp, session);
                break;
            case "/profile":
            default:
                showProfile(req, resp, session);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(Constant.SESSION_ACCOUNT) == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String path = req.getServletPath();

        if ("/verify-email-otp".equals(path)) {
            processVerifyEmailOtp(req, resp, session);
        } else {
            processUpdateProfile(req, resp, session);
        }
    }

    // 1. Hiển thị trang Profile người dùng
    private void showProfile(HttpServletRequest req, HttpServletResponse resp, HttpSession session)
            throws ServletException, IOException {

        User sessionUser = (User) session.getAttribute(Constant.SESSION_ACCOUNT);
        // Tải lại thông tin mới nhất từ cơ sở dữ liệu
        User currentUser = userService.get(sessionUser.getUserName());
        if (currentUser == null) {
            currentUser = sessionUser;
        } else {
            session.setAttribute(Constant.SESSION_ACCOUNT, currentUser);
        }

        req.setAttribute("user", currentUser);
        req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
    }

    // 2. Xử lý cập nhật thông tin Profile (Multipart Form)
    private void processUpdateProfile(HttpServletRequest req, HttpServletResponse resp, HttpSession session)
            throws ServletException, IOException {

        User sessionUser = (User) session.getAttribute(Constant.SESSION_ACCOUNT);
        User currentUser = userService.get(sessionUser.getUserName());
        if (currentUser == null) {
            currentUser = sessionUser;
        }

        String fullname = req.getParameter("fullname");
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");

        // Giữ lại các giá trị trên form để hiển thị lại nếu có lỗi
        req.setAttribute("inputFullname", fullname);
        req.setAttribute("inputPhone", phone);
        req.setAttribute("inputEmail", email);
        req.setAttribute("user", currentUser);

        // a. Validation cơ bản
        if (ValidationUtil.isEmpty(fullname) || ValidationUtil.isEmpty(email)) {
            req.setAttribute("alert", "Họ và tên và Địa chỉ Email không được để trống!");
            req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
            return;
        }

        if (fullname.trim().length() < 2 || fullname.trim().length() > 100) {
            req.setAttribute("alert", "Họ và tên phải có độ dài từ 2 đến 100 ký tự!");
            req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
            return;
        }

        if (!ValidationUtil.isValidEmail(email)) {
            req.setAttribute("alert", "Địa chỉ email không đúng định dạng (Ví dụ: name@example.com)!");
            req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
            return;
        }

        if (!ValidationUtil.isEmpty(phone) && !ValidationUtil.isValidPhone(phone)) {
            req.setAttribute("alert", "Số điện thoại không hợp lệ! Vui lòng nhập đúng 10 số bắt đầu bằng 03, 05, 07, 08, 09.");
            req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
            return;
        }

        // b. Kiểm tra trùng số điện thoại với tài khoản khác
        String trimmedPhone = phone != null ? phone.trim() : "";
        if (!trimmedPhone.isEmpty() && !trimmedPhone.equals(currentUser.getPhone()) && userService.checkExistPhone(trimmedPhone)) {
            req.setAttribute("alert", "Số điện thoại này đã được sử dụng bởi một tài khoản khác!");
            req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
            return;
        }

        // c. Xử lý Upload file ảnh lên Cloudinary bằng Multipart
        String uploadedAvatarUrl = null;
        try {
            Part filePart = req.getPart("image");
            if (filePart != null && filePart.getSize() > 0) {
                String contentType = filePart.getContentType();
                if (contentType == null || !contentType.toLowerCase().startsWith("image/")) {
                    req.setAttribute("alert", "File tải lên phải là định dạng hình ảnh hợp lệ (JPG, PNG, WEBP, GIF)!");
                    req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
                    return;
                }

                uploadedAvatarUrl = CloudinaryUtil.uploadImage(filePart);
            }
        } catch (Exception e) {
            System.err.println(">>> Lỗi khi upload ảnh lên Cloudinary: " + e.getMessage());
            req.setAttribute("alert", "Không thể upload ảnh lên Cloudinary: " + e.getMessage());
            req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
            return;
        }

        String finalAvatar = uploadedAvatarUrl != null ? uploadedAvatarUrl : currentUser.getAvatar();
        String trimmedEmail = email.trim();

        // d. Kiểm tra xem người dùng có đổi sang Email MỚI hay không
        boolean isEmailChanged = !trimmedEmail.equalsIgnoreCase(currentUser.getEmail());

        if (!isEmailChanged) {
            // Không đổi email -> Cập nhật trực tiếp ngay lập tức
            currentUser.setFullName(fullname.trim());
            currentUser.setPhone(trimmedPhone.isEmpty() ? null : trimmedPhone);
            currentUser.setAvatar(finalAvatar);

            userService.update(currentUser);
            session.setAttribute(Constant.SESSION_ACCOUNT, currentUser);

            resp.sendRedirect(req.getContextPath() + "/profile?msg=update_success");
        } else {
            // Có đổi email mới -> Kiểm tra email mới đã tồn tại trên hệ thống chưa
            if (userService.checkExistEmail(trimmedEmail)) {
                req.setAttribute("alert", "Email mới [" + trimmedEmail + "] đã được sử dụng bởi một tài khoản khác!");
                req.getRequestDispatcher(Constant.Path.PROFILE).forward(req, resp);
                return;
            }

            // Sinh mã OTP 6 số ngẫu nhiên để gửi đến EMAIL MỚI
            String otp = String.format("%06d", new Random().nextInt(1000000));
            long otpExpiry = System.currentTimeMillis() + 5 * 60 * 1000; // 5 phút

            // Lưu các thông tin chờ cập nhật vào Session
            session.setAttribute("pending_new_email", trimmedEmail);
            session.setAttribute("pending_fullname", fullname.trim());
            session.setAttribute("pending_phone", trimmedPhone.isEmpty() ? null : trimmedPhone);
            session.setAttribute("pending_avatar", finalAvatar);
            session.setAttribute("email_update_otp", otp);
            session.setAttribute("email_update_otp_expiry", otpExpiry);
            session.setAttribute("dev_email_otp", otp); // Để hiển thị hỗ trợ kiểm thử

            // Gửi email OTP đến EMAIL MỚI
            EmailUtil.sendUpdateEmailOtp(trimmedEmail, fullname.trim(), otp);

            resp.sendRedirect(req.getContextPath() + "/verify-email-otp");
        }
    }

    // 3. Hiển thị trang nhập OTP xác nhận Email mới
    private void showVerifyEmailOtp(HttpServletRequest req, HttpServletResponse resp, HttpSession session)
            throws ServletException, IOException {

        String pendingEmail = (String) session.getAttribute("pending_new_email");
        if (pendingEmail == null || pendingEmail.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        req.setAttribute("newEmail", pendingEmail);
        req.getRequestDispatcher(Constant.Path.VERIFY_EMAIL_OTP).forward(req, resp);
    }

    // 4. Xử lý xác nhận OTP để hoàn tất đổi Email mới
    private void processVerifyEmailOtp(HttpServletRequest req, HttpServletResponse resp, HttpSession session)
            throws ServletException, IOException {

        String pendingEmail = (String) session.getAttribute("pending_new_email");
        String pendingFullname = (String) session.getAttribute("pending_fullname");
        String pendingPhone = (String) session.getAttribute("pending_phone");
        String pendingAvatar = (String) session.getAttribute("pending_avatar");
        String serverOtp = (String) session.getAttribute("email_update_otp");
        Long otpExpiry = (Long) session.getAttribute("email_update_otp_expiry");

        if (pendingEmail == null || serverOtp == null) {
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        req.setAttribute("newEmail", pendingEmail);

        String clientOtp = req.getParameter("otp");

        // Validation mã OTP
        if (ValidationUtil.isEmpty(clientOtp)) {
            req.setAttribute("alert", "Vui lòng nhập mã OTP xác thực!");
            req.getRequestDispatcher(Constant.Path.VERIFY_EMAIL_OTP).forward(req, resp);
            return;
        }

        if (!ValidationUtil.isValidOtp(clientOtp)) {
            req.setAttribute("alert", "Mã OTP phải gồm đúng 6 chữ số!");
            req.getRequestDispatcher(Constant.Path.VERIFY_EMAIL_OTP).forward(req, resp);
            return;
        }

        // Kiểm tra hạn sử dụng OTP
        if (otpExpiry != null && System.currentTimeMillis() > otpExpiry) {
            req.setAttribute("alert", "Mã OTP đã hết hiệu lực (quá 5 phút)! Vui lòng nhấn 'Gửi lại mã OTP'.");
            req.getRequestDispatcher(Constant.Path.VERIFY_EMAIL_OTP).forward(req, resp);
            return;
        }

        // Kiểm tra mã OTP chính xác
        if (!serverOtp.equals(clientOtp.trim())) {
            req.setAttribute("alert", "Mã OTP không chính xác! Vui lòng kiểm tra lại trong hòm thư email mới.");
            req.getRequestDispatcher(Constant.Path.VERIFY_EMAIL_OTP).forward(req, resp);
            return;
        }

        // OTP hợp lệ -> Cập nhật thông tin vào CSDL
        User sessionUser = (User) session.getAttribute(Constant.SESSION_ACCOUNT);
        User currentUser = userService.get(sessionUser.getUserName());
        if (currentUser != null) {
            currentUser.setEmail(pendingEmail);
            currentUser.setFullName(pendingFullname != null ? pendingFullname : currentUser.getFullName());
            currentUser.setPhone(pendingPhone);
            if (pendingAvatar != null) {
                currentUser.setAvatar(pendingAvatar);
            }

            userService.update(currentUser);
            session.setAttribute(Constant.SESSION_ACCOUNT, currentUser);
        }

        // Xóa các session attribute tạm
        session.removeAttribute("pending_new_email");
        session.removeAttribute("pending_fullname");
        session.removeAttribute("pending_phone");
        session.removeAttribute("pending_avatar");
        session.removeAttribute("email_update_otp");
        session.removeAttribute("email_update_otp_expiry");
        session.removeAttribute("dev_email_otp");

        resp.sendRedirect(req.getContextPath() + "/profile?msg=email_update_success");
    }

    // 5. Gửi lại mã OTP xác nhận Email mới
    private void processResendEmailOtp(HttpServletRequest req, HttpServletResponse resp, HttpSession session)
            throws IOException {

        String pendingEmail = (String) session.getAttribute("pending_new_email");
        String pendingFullname = (String) session.getAttribute("pending_fullname");

        if (pendingEmail != null && !pendingEmail.trim().isEmpty()) {
            String newOtp = String.format("%06d", new Random().nextInt(1000000));
            long newExpiry = System.currentTimeMillis() + 5 * 60 * 1000;

            session.setAttribute("email_update_otp", newOtp);
            session.setAttribute("email_update_otp_expiry", newExpiry);
            session.setAttribute("dev_email_otp", newOtp);

            EmailUtil.sendUpdateEmailOtp(pendingEmail, pendingFullname != null ? pendingFullname : "Bạn", newOtp);
            resp.sendRedirect(req.getContextPath() + "/verify-email-otp?msg=resent");
        } else {
            resp.sendRedirect(req.getContextPath() + "/profile");
        }
    }
}
