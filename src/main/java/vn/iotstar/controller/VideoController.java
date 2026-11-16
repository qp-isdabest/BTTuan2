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

@WebServlet(urlPatterns = {"/videos", "/video/add", "/video/edit", "/video/delete"})
public class VideoController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final VideoService videoService = new VideoServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();

        switch (path) {
            case "/video/add":
                showAddForm(req, resp);
                break;
            case "/video/edit":
                showEditForm(req, resp);
                break;
            case "/video/delete":
                processDelete(req, resp);
                break;
            case "/videos":
            default:
                listVideos(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();

        if ("/video/add".equals(path)) {
            processAdd(req, resp);
        } else if ("/video/edit".equals(path)) {
            processEdit(req, resp);
        } else if ("/video/delete".equals(path)) {
            processDelete(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/videos");
        }
    }

    // 1. READ: Hiển thị danh sách Video (có hỗ trợ tìm kiếm theo tiêu đề)
    private void listVideos(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String keyword = req.getParameter("keyword");
        List<Video> videos;
        if (keyword != null && !keyword.trim().isEmpty()) {
            videos = videoService.searchByTitle(keyword.trim());
            req.setAttribute("keyword", keyword.trim());
        } else {
            videos = videoService.findAll();
        }

        req.setAttribute("videos", videos);
        req.getRequestDispatcher(Constant.Path.VIDEO_LIST).forward(req, resp);
    }

    // 2. CREATE (Form): Hiển thị form tạo mới video
    private void showAddForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(Constant.SESSION_ACCOUNT) == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        req.setAttribute("isEdit", false);
        req.getRequestDispatcher(Constant.Path.VIDEO_FORM).forward(req, resp);
    }

    // 2. CREATE (Action): Xử lý lưu video mới vào CSDL qua JPA
    private void processAdd(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(Constant.SESSION_ACCOUNT) == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        User currentUser = (User) session.getAttribute(Constant.SESSION_ACCOUNT);

        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String poster = req.getParameter("poster");
        String url = req.getParameter("url");
        String activeStr = req.getParameter("active");
        boolean active = "on".equals(activeStr) || "true".equals(activeStr);

        if (title == null || title.trim().isEmpty()) {
            req.setAttribute("alert", "Tiêu đề video không được để trống!");
            req.setAttribute("isEdit", false);
            req.getRequestDispatcher(Constant.Path.VIDEO_FORM).forward(req, resp);
            return;
        }

        Video newVideo = new Video(
                title.trim(),
                description != null ? description.trim() : "",
                poster != null && !poster.trim().isEmpty() ? poster.trim() : "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=600",
                url != null ? url.trim() : "",
                0,
                active,
                currentUser // Gán người tạo là User đang đăng nhập (Mối quan hệ 1 - N)
        );

        videoService.insert(newVideo);
        resp.sendRedirect(req.getContextPath() + "/videos?msg=add_success");
    }

    // 3. UPDATE (Form): Hiển thị form chỉnh sửa video
    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(Constant.SESSION_ACCOUNT) == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/videos");
            return;
        }

        try {
            int videoId = Integer.parseInt(idStr);
            Video video = videoService.findById(videoId);
            if (video == null) {
                resp.sendRedirect(req.getContextPath() + "/videos");
                return;
            }

            req.setAttribute("video", video);
            req.setAttribute("isEdit", true);
            req.getRequestDispatcher(Constant.Path.VIDEO_FORM).forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/videos");
        }
    }

    // 3. UPDATE (Action): Cập nhật thông tin video qua JPA
    private void processEdit(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(Constant.SESSION_ACCOUNT) == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("videoId");
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        String poster = req.getParameter("poster");
        String url = req.getParameter("url");
        String activeStr = req.getParameter("active");
        boolean active = "on".equals(activeStr) || "true".equals(activeStr);

        try {
            int videoId = Integer.parseInt(idStr);
            Video video = videoService.findById(videoId);
            if (video != null) {
                video.setTitle(title != null ? title.trim() : "");
                video.setDescription(description != null ? description.trim() : "");
                if (poster != null && !poster.trim().isEmpty()) {
                    video.setPoster(poster.trim());
                }
                video.setUrl(url != null ? url.trim() : "");
                video.setActive(active);

                videoService.update(video);
            }
            resp.sendRedirect(req.getContextPath() + "/videos?msg=edit_success");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/videos?msg=error");
        }
    }

    // 4. DELETE: Xóa video khỏi CSDL qua JPA
    private void processDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute(Constant.SESSION_ACCOUNT) == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String idStr = req.getParameter("id");
        if (idStr != null) {
            try {
                int videoId = Integer.parseInt(idStr);
                videoService.delete(videoId);
                resp.sendRedirect(req.getContextPath() + "/videos?msg=delete_success");
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        resp.sendRedirect(req.getContextPath() + "/videos");
    }
}
