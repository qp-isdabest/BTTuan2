package vn.iotstar.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import vn.iotstar.entity.User;
import vn.iotstar.entity.Video;
import vn.iotstar.service.UserService;
import vn.iotstar.service.VideoService;
import vn.iotstar.service.impl.UserServiceImpl;
import vn.iotstar.service.impl.VideoServiceImpl;
import vn.iotstar.util.PasswordUtil;

import java.sql.Date;

@WebListener
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println(">>> AppInitializer: Initializing database and default data...");
        try {
            UserService userService = new UserServiceImpl();
            String defaultHashedPassword = PasswordUtil.hashPassword("123456");

            // Khởi tạo tài khoản mẫu Admin nếu chưa tồn tại
            if (!userService.checkExistUsername("admin")) {
                User admin = new User(
                        "admin@iotstar.vn",
                        "admin",
                        "Hệ thống Admin",
                        defaultHashedPassword,
                        null,
                        1, // Admin
                        "0908123456",
                        new Date(System.currentTimeMillis()));
                userService.insert(admin);
                System.out.println(">>> Created default admin account: admin / 123456 (BCrypt Hashed)");
            }

            // Khởi tạo tài khoản mẫu Manager nếu chưa tồn tại
            if (!userService.checkExistUsername("manager")) {
                User manager = new User(
                        "manager@iotstar.vn",
                        "manager",
                        "Quản lý cửa hàng",
                        defaultHashedPassword,
                        null,
                        2, // Manager
                        "0908654321",
                        new Date(System.currentTimeMillis()));
                userService.insert(manager);
                System.out.println(">>> Created default manager account: manager / 123456 (BCrypt Hashed)");
            }

            // Khởi tạo tài khoản mẫu User nếu chưa tồn tại
            if (!userService.checkExistUsername("user")) {
                User user = new User(
                        "user@iotstar.vn",
                        "user",
                        "Phan Thế Quân",
                        defaultHashedPassword,
                        null,
                        3, // Regular User
                        "0912345678",
                        new Date(System.currentTimeMillis()));
                userService.insert(user);
                System.out.println(">>> Created default user account: user / 123456 (BCrypt Hashed)");
            }

            // Khởi tạo dữ liệu mẫu cho bảng Videos (Mối quan hệ 1-N với User)
            // Đảm bảo user có ít nhất 8 video và toàn hệ thống có 16 video để phân trang 6sp/trang rõ ràng
            VideoService videoService = new VideoServiceImpl();
            if (videoService.findAll().size() < 12) {
                User adminUser = userService.get("admin");
                User managerUser = userService.get("manager");
                User normalUser = userService.get("user");

                // Dọn dẹp video cũ nếu có ít hơn 12 để nạp lại đầy đủ bộ dữ liệu phân trang
                for (Video ev : videoService.findAll()) {
                    try {
                        videoService.delete(ev.getVideoId());
                    } catch (Exception ignored) {}
                }

                // 1. Danh sách video của normalUser (user - Phan Thế Quân): 8 video -> Phân trang: Trang 1 (6 sp), Trang 2 (2 sp)
                if (normalUser != null) {
                    Video[] userVideos = new Video[] {
                        new Video(
                            "01. Báo cáo bài tập Tuần 2 - Lập trình Web HCMUTE - Phan Thế Quân",
                            "Video demo đầy đủ các tính năng: Kiến trúc 3 tầng, JPA, Session, Cookie, OTP Email, BCrypt, Videos CRUD",
                            "https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=600",
                            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                            5280,
                            true,
                            normalUser
                        ),
                        new Video(
                            "02. Hướng dẫn xây dựng tính năng Xác thực Cookie và Session trong Servlet",
                            "Thực hành quản lý phiên làm việc với Session và Cookie ghi nhớ đăng nhập (Remember Me) 7 ngày",
                            "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=600",
                            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                            3120,
                            true,
                            normalUser
                        ),
                        new Video(
                            "03. Bảo mật mật khẩu người dùng với thuật toán BCrypt Hash và Salt",
                            "Áp dụng jBCrypt 12 rounds băm mật khẩu khi đăng ký, đăng nhập và đặt lại mật khẩu",
                            "https://images.unsplash.com/photo-1563986768609-322da13575f3?w=600",
                            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                            1450,
                            true,
                            normalUser
                        ),
                        new Video(
                            "04. Tích hợp Angus Jakarta Mail gửi mã OTP xác thực kích hoạt tài khoản",
                            "Quy trình sinh mã OTP 6 chữ số ngẫu nhiên có hạn 5 phút và kích hoạt tài khoản qua Email",
                            "https://images.unsplash.com/photo-1557200134-90327ee9fafa?w=600",
                            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                            2210,
                            true,
                            normalUser
                        ),
                        new Video(
                            "05. Lập trình tính năng Quên mật khẩu xác thực OTP gửi qua Email",
                            "Xác minh danh tính người dùng thông qua mã OTP email và cập nhật mật khẩu mới an toàn",
                            "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?w=600",
                            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                            1780,
                            true,
                            normalUser
                        ),
                        new Video(
                            "06. Thiết kế Entity Videos và mối quan hệ 1-N với Users trong Hibernate JPA",
                            "Cấu hình khóa ngoại user_id, sử dụng Annotation @ManyToOne và @OneToMany trong JPA 3.1",
                            "https://images.unsplash.com/photo-1544383835-bda2bc66a55d?w=600",
                            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                            2050,
                            true,
                            normalUser
                        ),
                        new Video(
                            "07. Lập trình CRUD Videos hoàn chỉnh với Servlet Controller và JSP Bootstrap 5",
                            "Các thao tác Thêm, Xem, Sửa, Xóa video kết nối cơ sở dữ liệu H2 Database qua EntityManager",
                            "https://images.unsplash.com/photo-1461749280684-dccba630e2f6?w=600",
                            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                            3400,
                            true,
                            normalUser
                        ),
                        new Video(
                            "08. Thuật toán Phân trang 6 sản phẩm/trang (Pagination 6sp/trang) trong JPA",
                            "Sử dụng setFirstResult và setMaxResults(6) để phân trang linh hoạt trên trang /video",
                            "https://images.unsplash.com/photo-1504639725590-34d0984388bd?w=600",
                            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                            4120,
                            true,
                            normalUser
                        )
                    };

                    for (Video v : userVideos) {
                        videoService.insert(v);
                    }
                }

                // 2. Danh sách video của adminUser: 5 video
                if (adminUser != null) {
                    Video[] adminVideos = new Video[] {
                        new Video(
                            "09. Kiến trúc 3 tầng và mô hình MVC trong Java Web Enterprise",
                            "Tách bạch rõ ràng giữa Presentation Layer, Business Logic Layer và Data Access Layer",
                            "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=600",
                            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                            2540,
                            true,
                            adminUser
                        ),
                        new Video(
                            "10. Cấu hình JPA persistence.xml và Hibernate ORM 6 trên Tomcat 11",
                            "Hướng dẫn chuẩn hóa cấu hình kết nối CSDL và tự động sinh bảng dữ liệu",
                            "https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=600",
                            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                            1890,
                            true,
                            adminUser
                        ),
                        new Video(
                            "11. Xây dựng WaitingController điều hướng phân quyền Admin, Manager, User",
                            "Kiểm tra vai trò roleid của tài khoản và chuyển hướng tới trang quản trị tương ứng",
                            "https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=600",
                            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                            2890,
                            true,
                            adminUser
                        ),
                        new Video(
                            "12. Triển khai ứng dụng Web WAR lên máy chủ Apache Tomcat 11",
                            "Đóng gói file WAR với Maven và chạy trên môi trường máy chủ cục bộ và Docker",
                            "https://images.unsplash.com/photo-1607799279861-4dd421887fb3?w=600",
                            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                            1670,
                            true,
                            adminUser
                        ),
                        new Video(
                            "13. Tối ưu hóa truy vấn Top 10 Video mới nhất hiển thị Trang chủ",
                            "Sử dụng JPQL JOIN FETCH kết hợp ORDER BY DESC và limit 10",
                            "https://images.unsplash.com/photo-1518770660439-4636190af475?w=600",
                            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                            2300,
                            true,
                            adminUser
                        )
                    };

                    for (Video v : adminVideos) {
                        videoService.insert(v);
                    }
                }

                // 3. Danh sách video của managerUser: 3 video
                if (managerUser != null) {
                    Video[] managerVideos = new Video[] {
                        new Video(
                            "14. Quản lý danh mục nội dung số và kiểm duyệt video",
                            "Hệ thống kiểm duyệt trạng thái active của video trước khi hiển thị cho người dùng",
                            "https://images.unsplash.com/photo-1522071820081-009f0129c71c?w=600",
                            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                            1950,
                            true,
                            managerUser
                        ),
                        new Video(
                            "15. Phân tích thống kê lượt xem (views) của video trên hệ thống",
                            "Báo cáo tương tác người dùng theo tháng và theo danh mục video",
                            "https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=600",
                            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                            1720,
                            true,
                            managerUser
                        ),
                        new Video(
                            "16. Quy chuẩn thiết kế giao diện responsive thân thiện người dùng",
                            "Áp dụng Bootstrap 5 Grid System tối ưu hiển thị trên Mobile và Desktop",
                            "https://images.unsplash.com/photo-1507238691740-187a5b1d37b8?w=600",
                            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                            2110,
                            true,
                            managerUser
                        )
                    };

                    for (Video v : managerVideos) {
                        videoService.insert(v);
                    }
                }
                System.out.println(">>> AppInitializer: Seeded 16 sample videos across users (User has 8 videos for 6sp/page pagination).");
            }
        } catch (Exception e) {
            System.err.println(">>> AppInitializer Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println(">>> AppInitializer: Closing JPA resources...");
        JpaConfig.close();
    }
}
