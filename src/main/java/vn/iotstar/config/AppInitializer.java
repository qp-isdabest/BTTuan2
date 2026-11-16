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
                        new Date(System.currentTimeMillis())
                );
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
                        new Date(System.currentTimeMillis())
                );
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
                        new Date(System.currentTimeMillis())
                );
                userService.insert(user);
                System.out.println(">>> Created default user account: user / 123456 (BCrypt Hashed)");
            }

            // Khởi tạo dữ liệu mẫu cho bảng Videos (Mối quan hệ 1-N với User)
            VideoService videoService = new VideoServiceImpl();
            if (videoService.findAll().isEmpty()) {
                User adminUser = userService.get("admin");
                User normalUser = userService.get("user");

                if (adminUser != null) {
                    Video v1 = new Video(
                            "Kiến trúc 3 tầng và mô hình MVC trong Java",
                            "Bài giảng chi tiết về kiến trúc 3 tầng và mô hình MVC2 trong lập trình web Java",
                            "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=600",
                            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                            2540,
                            true,
                            adminUser
                    );
                    Video v2 = new Video(
                            "Hướng dẫn sử dụng JPA và Hibernate với Servlet",
                            "Thao tác cơ sở dữ liệu với Jakarta Persistence 3.1 và Hibernate ORM",
                            "https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=600",
                            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                            1890,
                            true,
                            adminUser
                    );
                    videoService.insert(v1);
                    videoService.insert(v2);
                }

                if (normalUser != null) {
                    Video v3 = new Video(
                            "Báo cáo bài tập Tuần 2 - Phan Thế Quân",
                            "Video demo xác thực Session, Cookie, OTP Email và JPA",
                            "https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=600",
                            "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                            920,
                            true,
                            normalUser
                    );
                    videoService.insert(v3);
                }
                System.out.println(">>> AppInitializer: Seeded sample videos with 1-N relationship to users.");
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

