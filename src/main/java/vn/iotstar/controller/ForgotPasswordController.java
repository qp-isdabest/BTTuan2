package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;
import vn.iotstar.util.ValidationUtil;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@WebServlet(urlPatterns = {"/forgot-password", "/resend-forgot-otp"})
public class ForgotPasswordController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();
        String email = req.getParameter("email");

        HttpSession session = req.getSession(false);
        if ((email == null || email.trim().isEmpty()) && session != null) {
            email = (String) session.getAttribute("resetEmail");
        }

        // Xử lý gửi lại OTP đặt lại mật khẩu: /resend-forgot-otp
        if ("/resend-forgot-otp".equals(path)) {
            if (email == null || email.trim().isEmpty()) {
                resp.sendRedirect(req.getContextPath() + "/forgot-password");
                return;
            }

            String newOtp = userService.sendForgotPasswordOtp(email.trim());
            if (newOtp != null) {
                if (session != null) {
                    session.setAttribute("devForgotOtp", newOtp);
                }
                req.setAttribute("successMsg", "Mã OTP mới đã được gửi vào email của bạn!");
            } else {
                req.setAttribute("alert", "Không tìm thấy tài khoản để gửi lại mã OTP!");
            }

            req.setAttribute("email", email.trim());
            req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
            return;
        }

        // Mặc định: Hiển thị form quên mật khẩu
        req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        String identifier = req.getParameter("identifier");
        req.setAttribute("identifier", identifier);

        // 1. Kiểm tra rỗng
        if (ValidationUtil.isEmpty(identifier)) {
            req.setAttribute("alert", "Vui lòng nhập Email hoặc Tên đăng nhập của bạn!");
            req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
            return;
        }

        String trimmedId = identifier.trim();

        // 2. Nếu người dùng nhập dạng email thì kiểm tra format email
        if (trimmedId.contains("@") && !ValidationUtil.isValidEmail(trimmedId)) {
            req.setAttribute("alert", "Định dạng email không hợp lệ (Ví dụ: user@example.com)!");
            req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
            return;
        }

        // 3. Nếu là tên đăng nhập thì kiểm tra độ dài tối thiểu
        if (!trimmedId.contains("@") && trimmedId.length() < 3) {
            req.setAttribute("alert", "Tên đăng nhập phải có độ dài ít nhất 3 ký tự!");
            req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
            return;
        }

        User user = userService.findByUsernameOrEmail(trimmedId);
        if (user == null) {
            req.setAttribute("alert", "Không tìm thấy tài khoản nào khớp với thông tin đã cung cấp!");
            req.getRequestDispatcher(Constant.Path.FORGOT_PASSWORD).forward(req, resp);
            return;
        }

        // Tạo mã OTP đặt lại mật khẩu và gửi email
        String otp = userService.sendForgotPasswordOtp(user.getEmail());

        HttpSession session = req.getSession(true);
        session.setAttribute("resetEmail", user.getEmail());
        session.setAttribute("devForgotOtp", otp); // Hỗ trợ chế độ thử nghiệm nội bộ

        String encodedEmail = URLEncoder.encode(user.getEmail(), StandardCharsets.UTF_8);
        resp.sendRedirect(req.getContextPath() + "/reset-password?email=" + encodedEmail);
    }
}
