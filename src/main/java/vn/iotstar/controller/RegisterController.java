package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;
import vn.iotstar.util.EmailUtil;
import vn.iotstar.util.ValidationUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@WebServlet(urlPatterns = "/register")
public class RegisterController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Kiểm tra nếu đã đăng nhập Session thì chuyển tiếp
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(Constant.SESSION_ACCOUNT) != null) {
            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }

        // Kiểm tra Cookie
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Constant.COOKIE_REMEMBER.equals(cookie.getName())) {
                    String username = cookie.getValue();
                    if (username != null && !username.trim().isEmpty()) {
                        User user = userService.get(username);
                        if (user != null) {
                            session = req.getSession(true);
                            session.setAttribute(Constant.SESSION_ACCOUNT, user);
                            session.setAttribute(Constant.SESSION_USERNAME, user.getUserName());
                            resp.sendRedirect(req.getContextPath() + "/waiting");
                            return;
                        }
                    }
                }
            }
        }

        req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String email = req.getParameter("email");
        String fullname = req.getParameter("fullname");
        String phone = req.getParameter("phone");

        // Giữ lại các giá trị đã nhập để fill lại form khi có lỗi
        req.setAttribute("username", username);
        req.setAttribute("email", email);
        req.setAttribute("fullname", fullname);
        req.setAttribute("phone", phone);

        String alertMsg;

        // 1. Kiểm tra các trường bắt buộc
        if (ValidationUtil.isEmpty(username)
                || ValidationUtil.isEmpty(password)
                || ValidationUtil.isEmpty(email)
                || ValidationUtil.isEmpty(fullname)) {
            alertMsg = "Vui lòng điền đầy đủ các thông tin bắt buộc (*)!";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        // 2. Kiểm tra định dạng tên đăng nhập (3 - 30 ký tự, không dấu cách, không ký tự đặc biệt ngoài _)
        if (!ValidationUtil.isValidUsername(username)) {
            alertMsg = "Tên đăng nhập không hợp lệ! Chỉ gồm chữ cái, chữ số, gạch dưới và có độ dài từ 3 đến 30 ký tự.";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        // 3. Kiểm tra độ dài họ và tên
        if (fullname.trim().length() < 2 || fullname.trim().length() > 100) {
            alertMsg = "Họ và tên phải có độ dài từ 2 đến 100 ký tự!";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        // 4. Kiểm tra định dạng Email chuẩn
        if (!ValidationUtil.isValidEmail(email)) {
            alertMsg = "Địa chỉ email không đúng định dạng (Ví dụ: user@example.com)!";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        // 5. Kiểm tra định dạng số điện thoại Việt Nam (nếu có nhập)
        if (!ValidationUtil.isValidPhone(phone)) {
            alertMsg = "Số điện thoại không hợp lệ! Vui lòng nhập đúng 10 số bắt đầu bằng 03, 05, 07, 08, 09.";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        // 6. Kiểm tra độ dài mật khẩu (ít nhất 6 ký tự)
        if (!ValidationUtil.isValidPassword(password)) {
            alertMsg = "Mật khẩu phải có độ dài tối thiểu từ 6 ký tự trở lên!";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        // 7. Kiểm tra trùng khớp mật khẩu xác nhận
        if (confirmPassword == null || !password.equals(confirmPassword)) {
            alertMsg = "Mật khẩu xác nhận không khớp với mật khẩu đã nhập!";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        // 8. Kiểm tra trùng lặp dữ liệu trong hệ thống
        if (userService.checkExistUsername(username.trim())) {
            alertMsg = "Tên đăng nhập này đã được sử dụng! Vui lòng chọn tên khác.";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        if (userService.checkExistEmail(email.trim())) {
            alertMsg = "Địa chỉ email này đã được đăng ký trên hệ thống!";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        if (phone != null && !phone.trim().isEmpty() && userService.checkExistPhone(phone.trim())) {
            alertMsg = "Số điện thoại này đã được gắn với một tài khoản khác!";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        // Sinh mã OTP 6 số ngẫu nhiên
        String otp = String.format("%06d", new Random().nextInt(1000000));
        long otpExpiry = System.currentTimeMillis() + 5 * 60 * 1000; // Hết hạn sau 5 phút

        boolean isSuccess = userService.registerWithOtp(
                username.trim(),
                password.trim(),
                email.trim(),
                fullname.trim(),
                phone != null ? phone.trim() : "",
                otp,
                otpExpiry
        );

        if (isSuccess) {
            // Gửi OTP qua email
            EmailUtil.sendOtpEmail(email.trim(), fullname.trim(), otp);

            // Lưu thông tin tạm để hỗ trợ kiểm thử tiện lợi
            HttpSession session = req.getSession(true);
            session.setAttribute("otpEmail", email.trim());
            session.setAttribute("devOtp", otp); // Hỗ trợ hiển thị trên UI khi chưa cài đặt SMTP thật

            String encodedEmail = URLEncoder.encode(email.trim(), StandardCharsets.UTF_8);
            resp.sendRedirect(req.getContextPath() + "/verify-otp?email=" + encodedEmail);
        } else {
            alertMsg = "Lỗi hệ thống khi đăng ký tài khoản!";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
        }
    }
}
