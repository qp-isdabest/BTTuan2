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
import vn.iotstar.util.ValidationUtil;

import java.io.IOException;

@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 1. Kiểm tra Session đã đăng nhập chưa
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(Constant.SESSION_ACCOUNT) != null) {
            resp.sendRedirect(req.getContextPath() + "/waiting");
            return;
        }

        // 2. Kiểm tra Cookie ghi nhớ đăng nhập
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (Constant.COOKIE_REMEMBER.equals(cookie.getName())) {
                    String username = cookie.getValue();
                    if (username != null && !username.trim().isEmpty()) {
                        User user = userService.get(username);
                        if (user != null && user.getStatus() == 1) {
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

        req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        req.setCharacterEncoding("UTF-8");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String remember = req.getParameter("remember");
        boolean isRememberMe = "on".equals(remember);

        String alertMsg;

        // 1. Validation kiểm tra rỗng
        if (ValidationUtil.isEmpty(username) || ValidationUtil.isEmpty(password)) {
            alertMsg = "Tài khoản và mật khẩu không được để trống!";
            req.setAttribute("alert", alertMsg);
            req.setAttribute("username", username);
            req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
            return;
        }

        // 2. Validation độ dài tài khoản
        if (username.trim().length() < 3) {
            alertMsg = "Tên đăng nhập phải có ít nhất 3 ký tự!";
            req.setAttribute("alert", alertMsg);
            req.setAttribute("username", username);
            req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
            return;
        }

        User user = userService.login(username.trim(), password.trim());

        if (user != null) {
            // Kiểm tra trạng thái kích hoạt tài khoản qua OTP
            if (user.getStatus() == 0) {
                alertMsg = "Tài khoản của bạn chưa được kích hoạt qua mã OTP!";
                req.setAttribute("alert", alertMsg);
                req.setAttribute("pendingEmail", user.getEmail());
                req.setAttribute("username", username);
                req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
                return;
            }

            // Đăng nhập thành công -> lưu phiên vào HttpSession
            HttpSession session = req.getSession(true);
            session.setAttribute(Constant.SESSION_ACCOUNT, user);
            session.setAttribute(Constant.SESSION_USERNAME, user.getUserName());

            // Xử lý Cookie "Ghi nhớ đăng nhập"
            if (isRememberMe) {
                saveRememberMe(req, resp, user.getUserName());
            } else {
                deleteRememberMe(req, resp);
            }

            resp.sendRedirect(req.getContextPath() + "/waiting");
        } else {
            alertMsg = "Tài khoản hoặc mật khẩu không đúng!";
            req.setAttribute("alert", alertMsg);
            req.setAttribute("username", username);
            req.getRequestDispatcher(Constant.Path.LOGIN).forward(req, resp);
        }
    }

    private void saveRememberMe(HttpServletRequest req, HttpServletResponse resp, String username) {
        Cookie cookie = new Cookie(Constant.COOKIE_REMEMBER, username);
        cookie.setMaxAge(30 * 60); // 30 phút (hoặc 7 ngày tùy cấu hình)
        cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
        cookie.setHttpOnly(true);
        resp.addCookie(cookie);
    }

    private void deleteRememberMe(HttpServletRequest req, HttpServletResponse resp) {
        Cookie cookie = new Cookie(Constant.COOKIE_REMEMBER, "");
        cookie.setMaxAge(0);
        cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
        resp.addCookie(cookie);
    }
}

