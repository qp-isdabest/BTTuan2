package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.util.Constant;

import java.io.IOException;

@WebServlet(urlPatterns = "/logout")
public class LogoutController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processLogout(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processLogout(req, resp);
    }

    private void processLogout(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        // 1. Hủy phiên làm việc HttpSession
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 2. Xóa Cookie ghi nhớ đăng nhập
        String contextPath = req.getContextPath().isEmpty() ? "/" : req.getContextPath();
        Cookie cookie = new Cookie(Constant.COOKIE_REMEMBER, "");
        cookie.setMaxAge(0);
        cookie.setPath(contextPath);
        resp.addCookie(cookie);

        // Xóa thêm ở root path "/" để đảm bảo xóa sạch cookie trên mọi trình duyệt
        Cookie rootCookie = new Cookie(Constant.COOKIE_REMEMBER, "");
        rootCookie.setMaxAge(0);
        rootCookie.setPath("/");
        resp.addCookie(rootCookie);

        // 3. Chuyển hướng về trang đăng nhập kèm cờ thông báo logout thành công
        resp.sendRedirect(req.getContextPath() + "/login?logout=1");
    }
}
