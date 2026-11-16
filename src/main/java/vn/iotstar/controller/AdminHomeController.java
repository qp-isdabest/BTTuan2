package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;
import vn.iotstar.entity.Video;
import vn.iotstar.service.UserService;
import vn.iotstar.service.VideoService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.service.impl.VideoServiceImpl;
import vn.iotstar.util.Constant;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/admin/home")
public class AdminHomeController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserService userService = new UserServiceImpl();
    private final VideoService videoService = new VideoServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(Constant.SESSION_ACCOUNT) == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User user = (User) session.getAttribute(Constant.SESSION_ACCOUNT);
        if (user.getRoleid() != 1) {
            // Không có quyền Admin -> quay về /home
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        // Lấy danh sách toàn bộ người dùng từ CSDL thông qua JPA Service
        List<User> userList = userService.findAll();
        req.setAttribute("adminUser", user);
        req.setAttribute("userList", userList);

        // Lấy danh sách toàn bộ video (kèm thông tin tác giả User) thông qua JPA Service
        List<Video> videoList = videoService.findAll();
        req.setAttribute("videoList", videoList);

        req.getRequestDispatcher(Constant.Path.ADMIN_HOME).forward(req, resp);
    }
}

