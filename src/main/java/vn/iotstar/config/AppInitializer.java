package vn.iotstar.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.service.impl.UserServiceImpl;

import java.sql.Date;

@WebListener
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println(">>> AppInitializer: Initializing database and default data...");
        try {
            UserService userService = new UserServiceImpl();

            // Khởi tạo tài khoản mẫu Admin nếu chưa tồn tại
            if (!userService.checkExistUsername("admin")) {
                User admin = new User(
                        "admin@iotstar.vn",
                        "admin",
                        "Hệ thống Admin",
                        "123456",
                        null,
                        1, // Admin
                        "0908123456",
                        new Date(System.currentTimeMillis())
                );
                userService.insert(admin);
                System.out.println(">>> Created default admin account: admin / 123456");
            }

            // Khởi tạo tài khoản mẫu Manager nếu chưa tồn tại
            if (!userService.checkExistUsername("manager")) {
                User manager = new User(
                        "manager@iotstar.vn",
                        "manager",
                        "Quản lý cửa hàng",
                        "123456",
                        null,
                        2, // Manager
                        "0908654321",
                        new Date(System.currentTimeMillis())
                );
                userService.insert(manager);
                System.out.println(">>> Created default manager account: manager / 123456");
            }

            // Khởi tạo tài khoản mẫu User nếu chưa tồn tại
            if (!userService.checkExistUsername("user")) {
                User user = new User(
                        "user@iotstar.vn",
                        "user",
                        "Phan Thế Quân",
                        "123456",
                        null,
                        3, // Regular User
                        "0912345678",
                        new Date(System.currentTimeMillis())
                );
                userService.insert(user);
                System.out.println(">>> Created default user account: user / 123456");
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

