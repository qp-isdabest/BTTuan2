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
import vn.iotstar.util.ValidationUtil;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/video", "/videos", "/video/add", "/video/edit", "/video/delete", "/video/detail"})
public class VideoController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 6; // Phân trang 6 video (6 sản phẩm) / trang theo yêu cầu
    private final VideoService videoService = new VideoServiceImpl();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();

        switch (path) {
            case "/video/detail":
                showVideoDetail(req, resp);
                break;
            case "/video/add":
                showAddForm(req, resp);
                break;
            case "/video/edit":
                showEditForm(req, resp);
                break;
            case "/video/delete":
                processDelete(req, resp);
                break;
            case "/video":
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
            resp.sendRedirect(req.getContextPath() + "/video");
        }
    }

    // 1. READ: Hiển thị danh sách Video của user và phân trang 6 sản phẩm/trang trên URL /video
    private void listVideos(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        User currentUser = (session != null) ? (User) session.getAttribute(Constant.SESSION_ACCOUNT) : null;

        // 1. Lấy trang hiện tại (page)
        int page = 1;
        String pageStr = req.getParameter("page");
        if (pageStr != null && !pageStr.trim().isEmpty()) {
            try {
                page = Integer.parseInt(pageStr.trim());
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        // 2. Lấy từ khóa tìm kiếm (keyword)
        String keyword = req.getParameter("keyword");
        if (keyword != null) {
            keyword = keyword.trim();
        }

        // 3. Xác định chế độ hiển thị:
        // filter = "my" (chỉ video của user hiện tại) hoặc "all" (toàn bộ video hệ thống)
        // Nếu user đã đăng nhập và không truyền filter -> mặc định hiển thị "my" (videos của user)
        String filter = req.getParameter("filter");
        if (filter == null || filter.trim().isEmpty()) {
            if (currentUser != null) {
                filter = "my"; // Ưu tiên hiển thị video của user theo yêu cầu đề bài
            } else {
                filter = "all";
            }
        }

        // Hỗ trợ thêm tham số userId tùy chọn để xem video của một user cụ thể
        String userIdStr = req.getParameter("userId");
        Integer targetUserId = null;
        if (userIdStr != null && !userIdStr.trim().isEmpty()) {
            try {
                targetUserId = Integer.parseInt(userIdStr.trim());
            } catch (NumberFormatException ignored) {}
        }

        List<Video> videos;
        long totalVideos = 0;

        if (targetUserId != null) {
            totalVideos = videoService.countByTitleAndUserId(keyword, targetUserId);
            videos = videoService.searchByTitleAndUserId(keyword, targetUserId, page, PAGE_SIZE);
        } else if ("my".equalsIgnoreCase(filter) && currentUser != null) {
            totalVideos = videoService.countByTitleAndUserId(keyword, currentUser.getId());
            videos = videoService.searchByTitleAndUserId(keyword, currentUser.getId(), page, PAGE_SIZE);
        } else {
            filter = "all";
            totalVideos = videoService.countByTitle(keyword);
            videos = videoService.searchByTitle(keyword, page, PAGE_SIZE);
        }

        // Tính tổng số trang
        int totalPages = (int) Math.ceil((double) totalVideos / PAGE_SIZE);
        if (totalPages == 0) {
            totalPages = 1;
        }
        if (page > totalPages) {
            page = totalPages;
        }

        // Số lượng video của riêng user và của toàn hệ thống (để hiển thị badge trên tab)
        if (currentUser != null) {
            long myCount = videoService.countByUserId(currentUser.getId());
            req.setAttribute("myVideoCount", myCount);
        }
        long allCount = videoService.countAll();
        req.setAttribute("allVideoCount", allCount);

        req.setAttribute("videos", videos);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("totalVideos", totalVideos);
        req.setAttribute("pageSize", PAGE_SIZE);
        req.setAttribute("filter", filter);
        req.setAttribute("keyword", keyword);
        req.setAttribute("targetUserId", targetUserId);

        req.getRequestDispatcher(Constant.Path.VIDEO_LIST).forward(req, resp);
    }

    // 1.1 DETAIL & PLAY: Hiển thị chi tiết và phát video
    private void showVideoDetail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idStr = req.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/video");
            return;
        }

        try {
            int videoId = Integer.parseInt(idStr.trim());
            Video video = videoService.findById(videoId);
            if (video == null) {
                resp.sendRedirect(req.getContextPath() + "/video");
                return;
            }

            // Tự động tăng lượt xem (view count + 1) khi phát video
            video.setViews(video.getViews() + 1);
            videoService.update(video);

            // Lấy danh sách video đề xuất (10 video mới nhất)
            List<Video> relatedVideos = videoService.findTop10LatestVideos();

            req.setAttribute("video", video);
            req.setAttribute("relatedVideos", relatedVideos);
            req.getRequestDispatcher(Constant.Path.VIDEO_DETAIL).forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/video");
        }
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

        // Chuẩn bị đối tượng tạm để giữ lại dữ liệu đã nhập nếu có lỗi validation
        Video tempVideo = new Video();
        tempVideo.setTitle(title != null ? title.trim() : "");
        tempVideo.setDescription(description != null ? description.trim() : "");
        tempVideo.setPoster(poster != null ? poster.trim() : "");
        tempVideo.setUrl(url != null ? url.trim() : "");
        tempVideo.setActive(active);

        // 1. Kiểm tra tiêu đề bắt buộc
        if (ValidationUtil.isEmpty(title)) {
            req.setAttribute("alert", "Tiêu đề video không được để trống!");
            req.setAttribute("video", tempVideo);
            req.setAttribute("isEdit", false);
            req.getRequestDispatcher(Constant.Path.VIDEO_FORM).forward(req, resp);
            return;
        }

        // 2. Kiểm tra độ dài tiêu đề
        if (title.trim().length() < 3 || title.trim().length() > 255) {
            req.setAttribute("alert", "Tiêu đề video phải có độ dài từ 3 đến 255 ký tự!");
            req.setAttribute("video", tempVideo);
            req.setAttribute("isEdit", false);
            req.getRequestDispatcher(Constant.Path.VIDEO_FORM).forward(req, resp);
            return;
        }

        // 3. Kiểm tra định dạng Poster URL (nếu có nhập)
        if (!ValidationUtil.isEmpty(poster) && !ValidationUtil.isValidUrl(poster)) {
            req.setAttribute("alert", "Đường dẫn ảnh thumbnail (Poster URL) không hợp lệ (phải bắt đầu bằng http:// hoặc https://)!");
            req.setAttribute("video", tempVideo);
            req.setAttribute("isEdit", false);
            req.getRequestDispatcher(Constant.Path.VIDEO_FORM).forward(req, resp);
            return;
        }

        // 4. Kiểm tra định dạng Video URL (nếu có nhập)
        if (!ValidationUtil.isEmpty(url) && !ValidationUtil.isValidUrl(url)) {
            req.setAttribute("alert", "Đường dẫn video (URL Link) không hợp lệ (phải bắt đầu bằng http:// hoặc https://)!");
            req.setAttribute("video", tempVideo);
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
        resp.sendRedirect(req.getContextPath() + "/video?msg=add_success");
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
            resp.sendRedirect(req.getContextPath() + "/video");
            return;
        }

        try {
            int videoId = Integer.parseInt(idStr);
            Video video = videoService.findById(videoId);
            if (video == null) {
                resp.sendRedirect(req.getContextPath() + "/video");
                return;
            }

            req.setAttribute("video", video);
            req.setAttribute("isEdit", true);
            req.getRequestDispatcher(Constant.Path.VIDEO_FORM).forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/video");
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

        int videoId = 0;
        try {
            videoId = Integer.parseInt(idStr);
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/video");
            return;
        }

        // Tạo đối tượng tạm để giữ lại dữ liệu form khi có lỗi
        Video tempVideo = new Video();
        tempVideo.setVideoId(videoId);
        tempVideo.setTitle(title != null ? title.trim() : "");
        tempVideo.setDescription(description != null ? description.trim() : "");
        tempVideo.setPoster(poster != null ? poster.trim() : "");
        tempVideo.setUrl(url != null ? url.trim() : "");
        tempVideo.setActive(active);

        // 1. Kiểm tra tiêu đề bắt buộc
        if (ValidationUtil.isEmpty(title)) {
            req.setAttribute("alert", "Tiêu đề video không được để trống!");
            req.setAttribute("video", tempVideo);
            req.setAttribute("isEdit", true);
            req.getRequestDispatcher(Constant.Path.VIDEO_FORM).forward(req, resp);
            return;
        }

        // 2. Kiểm tra độ dài tiêu đề
        if (title.trim().length() < 3 || title.trim().length() > 255) {
            req.setAttribute("alert", "Tiêu đề video phải có độ dài từ 3 đến 255 ký tự!");
            req.setAttribute("video", tempVideo);
            req.setAttribute("isEdit", true);
            req.getRequestDispatcher(Constant.Path.VIDEO_FORM).forward(req, resp);
            return;
        }

        // 3. Kiểm tra định dạng Poster URL
        if (!ValidationUtil.isEmpty(poster) && !ValidationUtil.isValidUrl(poster)) {
            req.setAttribute("alert", "Đường dẫn ảnh thumbnail (Poster URL) không hợp lệ (phải bắt đầu bằng http:// hoặc https://)!");
            req.setAttribute("video", tempVideo);
            req.setAttribute("isEdit", true);
            req.getRequestDispatcher(Constant.Path.VIDEO_FORM).forward(req, resp);
            return;
        }

        // 4. Kiểm tra định dạng Video URL
        if (!ValidationUtil.isEmpty(url) && !ValidationUtil.isValidUrl(url)) {
            req.setAttribute("alert", "Đường dẫn video (URL Link) không hợp lệ (phải bắt đầu bằng http:// hoặc https://)!");
            req.setAttribute("video", tempVideo);
            req.setAttribute("isEdit", true);
            req.getRequestDispatcher(Constant.Path.VIDEO_FORM).forward(req, resp);
            return;
        }

        try {
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
            resp.sendRedirect(req.getContextPath() + "/video?msg=edit_success");
        } catch (Exception e) {
            e.printStackTrace();
            resp.sendRedirect(req.getContextPath() + "/video?msg=error");
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
                resp.sendRedirect(req.getContextPath() + "/video?msg=delete_success");
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        resp.sendRedirect(req.getContextPath() + "/video");
    }
}
