package vn.iotstar.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.iotstar.entity.User;
import vn.iotstar.entity.Video;
import vn.iotstar.service.VideoService;
import vn.iotstar.service.impl.VideoServiceImpl;
import vn.iotstar.util.Constant;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = "/home")
public class HomeController extends HttpServlet {

    private static final long serialVersionUID = 1L;
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
        req.setAttribute("user", user);

        // Lấy danh sách video của người dùng hiện tại (1 - N)
        List<Video> myVideos = videoService.findByUserId(user.getId());
        req.setAttribute("myVideos", myVideos);

        // Lấy toàn bộ danh sách video trên hệ thống
        List<Video> allVideos = videoService.findAll();
        req.setAttribute("allVideos", allVideos);

        req.getRequestDispatcher(Constant.Path.HOME).forward(req, resp);
    }
}

