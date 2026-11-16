package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.util.Constant;

import java.io.IOException;

@WebServlet(urlPatterns = "/reset-password")
public class ResetPasswordController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        HttpSession session = req.getSession(false);
        if ((email == null || email.trim().isEmpty()) && session != null) {
            email = (String) session.getAttribute("resetEmail");
        }

        if (email == null || email.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/forgot-password");
            return;
        }

        req.setAttribute("email", email.trim());
        req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String otp = req.getParameter("otp");
        String newPassword = req.getParameter("newPassword");
        String confirmPassword = req.getParameter("confirmPassword");

        req.setAttribute("email", email);

        if (email == null || email.trim().isEmpty()
                || otp == null || otp.trim().isEmpty()
                || newPassword == null || newPassword.trim().isEmpty()) {
            req.setAttribute("alert", "Vui lòng nhập đầy đủ mã OTP và mật khẩu mới!");
            req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
            return;
        }

        if (confirmPassword != null && !newPassword.equals(confirmPassword)) {
            req.setAttribute("alert", "Mật khẩu xác nhận không khớp!");
            req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
            return;
        }

        if (newPassword.length() < 6) {
            req.setAttribute("alert", "Mật khẩu mới phải có độ dài ít nhất 6 ký tự!");
            req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
            return;
        }

        int result = userService.resetPassword(email.trim(), otp.trim(), newPassword.trim());

        switch (result) {
            case 1:
                // Thành công
                HttpSession session = req.getSession(false);
                if (session != null) {
                    session.removeAttribute("devForgotOtp");
                    session.removeAttribute("resetEmail");
                }
                req.setAttribute("successMsg", "Đặt lại mật khẩu thành công! Vui lòng đăng nhập với mật khẩu mới.");
                req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
                break;

            case 0:
                // Sai OTP
                req.setAttribute("alert", "Mã OTP không chính xác! Vui lòng kiểm tra lại trong email.");
                req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
                break;

            case -1:
                // Hết hạn OTP
                req.setAttribute("alert", "Mã OTP đã hết hiệu lực (quá 5 phút)! Vui lòng nhấn 'Gửi lại mã OTP'.");
                req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
                break;

            case -2:
            default:
                // Không tìm thấy user
                req.setAttribute("alert", "Không tìm thấy thông tin tài khoản hợp lệ!");
                req.getRequestDispatcher(Constant.Path.RESET_PASSWORD).forward(req, resp);
                break;
        }
    }
}
