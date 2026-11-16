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

@WebServlet(urlPatterns = {"/verify-otp", "/resend-otp"})
public class VerifyOtpController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();
        String email = req.getParameter("email");

        HttpSession session = req.getSession(false);
        if ((email == null || email.trim().isEmpty()) && session != null) {
            email = (String) session.getAttribute("otpEmail");
        }

        if (email == null || email.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/register");
            return;
        }

        // Xử lý gửi lại OTP: /resend-otp
        if ("/resend-otp".equals(path)) {
            String newOtp = userService.resendOtp(email.trim());
            if (newOtp != null) {
                if (session != null) {
                    session.setAttribute("devOtp", newOtp);
                }
                req.setAttribute("successMsg", "Mã OTP mới đã được gửi vào email của bạn!");
            } else {
                req.setAttribute("alert", "Không thể gửi lại mã OTP. Tài khoản không tồn tại!");
            }
        }

        req.setAttribute("email", email.trim());
        req.getRequestDispatcher(Constant.Path.VERIFY_OTP).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("email");
        String otp = req.getParameter("otp");

        req.setAttribute("email", email);

        if (email == null || email.trim().isEmpty() || otp == null || otp.trim().isEmpty()) {
            req.setAttribute("alert", "Vui lòng nhập đầy đủ mã OTP!");
            req.getRequestDispatcher(Constant.Path.VERIFY_OTP).forward(req, resp);
            return;
        }

        int result = userService.verifyOtp(email.trim(), otp.trim());

        switch (result) {
            case 1:
                // Thành công
                HttpSession session = req.getSession(false);
                if (session != null) {
                    session.removeAttribute("devOtp");
                    session.removeAttribute("otpEmail");
                }
                req.setAttribute("successMsg", "Kích hoạt tài khoản thành công! Bạn có thể đăng nhập ngay bây giờ.");
                req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
                break;

            case 0:
                // Sai OTP
                req.setAttribute("alert", "Mã OTP không chính xác! Vui lòng kiểm tra lại trong email.");
                req.getRequestDispatcher(Constant.Path.VERIFY_OTP).forward(req, resp);
                break;

            case -1:
                // Hết hạn
                req.setAttribute("alert", "Mã OTP đã hết hiệu lực (quá 5 phút)! Vui lòng bấm 'Gửi lại mã OTP'.");
                req.getRequestDispatcher(Constant.Path.VERIFY_OTP).forward(req, resp);
                break;

            case -2:
            default:
                // Không tìm thấy user
                req.setAttribute("alert", "Tài khoản cần kích hoạt không tồn tại trong hệ thống!");
                req.getRequestDispatcher(Constant.Path.VERIFY_OTP).forward(req, resp);
                break;
        }
    }
}
