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

import java.io.IOException;

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

        if (username == null || username.trim().isEmpty()
                || password == null || password.trim().isEmpty()
                || email == null || email.trim().isEmpty()
                || fullname == null || fullname.trim().isEmpty()) {
            alertMsg = "Vui lòng điền đầy đủ các thông tin bắt buộc!";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        if (confirmPassword != null && !password.equals(confirmPassword)) {
            alertMsg = "Mật khẩu xác nhận không khớp!";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        if (userService.checkExistEmail(email)) {
            alertMsg = "Email đã tồn tại!";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        if (userService.checkExistUsername(username)) {
            alertMsg = "Tài khoản đã tồn tại!";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        if (phone != null && !phone.trim().isEmpty() && userService.checkExistPhone(phone)) {
            alertMsg = "Số điện thoại đã tồn tại!";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
            return;
        }

        boolean isSuccess = userService.register(
                username.trim(),
                password.trim(),
                email.trim(),
                fullname.trim(),
                phone != null ? phone.trim() : ""
        );

        if (isSuccess) {
            req.setAttribute("successMsg", "Đăng ký tài khoản thành công! Vui lòng đăng nhập.");
            req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
        } else {
            alertMsg = "Lỗi hệ thống khi đăng ký tài khoản!";
            req.setAttribute("alert", alertMsg);
            req.getRequestDispatcher(Constant.Path.REGISTER).forward(req, resp);
        }
    }
}

