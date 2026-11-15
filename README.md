# BÀI TẬP TUẦN 2 - LẬP TRÌNH WEB (WEBPR330479)
**Họ và tên:** Phan Thế Quân  
**MSSV:** 24110050  
**Lớp / Môn:** Lập Trình Web - ThS. Nguyễn Hữu Trung  
**Chủ đề:** Kiến trúc 3 tầng (Three-Tier Architecture), MVC Model 2, Đăng nhập với Session & Cookie, Đăng ký người dùng và Công nghệ JPA (Jakarta Persistence 3.1 & Hibernate).

---

## 1. Cấu trúc dự án 3 Tầng (Three-Tier Architecture)


```
24110050_PhanTheQuan_Week2/
├── pom.xml                                      # Cấu hình Maven (Jakarta Servlet 6.0, JPA 3.1, Hibernate 6, H2 DB)
├── run-tomcat.ps1                              # Script 1-click build và khởi chạy Tomcat 11
├── .vscode/tasks.json                           # Task tích hợp trong VS Code
├── src/main/resources/
│   └── META-INF/
│       └── persistence.xml                     # Cấu hình Persistence Unit (UserPU) kết nối CSDL qua JPA
├── src/main/java/vn/iotstar/
│   ├── entity/
│   │   └── User.java                           # Model / Entity ánh xạ CSDL qua JPA (@Entity, @Table, @Id)
│   ├── config/
│   │   ├── JpaConfig.java                      # Cấu hình khởi tạo EntityManagerFactory & EntityManager
│   │   └── AppInitializer.java                 # WebListener tự khởi tạo dữ liệu mẫu khi start web app
│   ├── dao/                                    # TẦNG 3: DATA ACCESS LAYER
│   │   ├── UserDao.java                        # Interface DAO định nghĩa các thao tác CRUD với CSDL
│   │   └── impl/
│   │       └── UserDaoImpl.java                # Triển khai truy vấn dữ liệu bằng JPA (JPQL, EntityManager)
│   ├── service/                                # TẦNG 2: BUSINESS LOGIC LAYER
│   │   ├── UserService.java                    # Interface Service định nghĩa logic nghiệp vụ
│   │   └── impl/
│   │       └── UserServiceImpl.java            # Triển khai logic đăng nhập, xác thực, đăng ký, kiểm tra trùng
│   ├── controller/                             # TẦNG 1: PRESENTATION LAYER (Controllers)
│   │   ├── LoginController.java                # Xử lý đăng nhập, lưu Session, lưu Cookie "Ghi nhớ tôi"
│   │   ├── RegisterController.java             # Xử lý đăng ký tài khoản mới
│   │   ├── WaitingController.java              # Điều hướng người dùng sau khi đăng nhập dựa trên Vai trò (Role)
│   │   ├── HomeController.java                 # Trang chủ hiển thị thông tin Session & Cookie của User
│   │   ├── AdminHomeController.java            # Bảng điều khiển Admin xem toàn bộ danh sách User từ JPA
│   │   └── LogoutController.java               # Hủy Session, xóa Cookie Remember-Me
│   └── util/
│       └── Constant.java                       # Hằng số định danh Session, Cookie, đường dẫn View
└── src/main/webapp/
    ├── WEB-INF/
    │   ├── web.xml                             # Deployment Descriptor
    │   └── views/                              # TẦNG 1: PRESENTATION LAYER (Views)
    │       ├── topbar.jsp                      # Header / Thanh điều hướng dùng chung
    │       ├── login.jsp                       # Giao diện Đăng nhập (hỗ trợ Remember Me qua Cookie)
    │       ├── register.jsp                    # Giao diện Đăng ký tài khoản
    │       ├── home.jsp                        # Giao diện Trang chủ người dùng (User & Manager)
    │       └── admin_home.jsp                  # Giao diện Trang quản trị Admin (xem danh sách CSDL)
    └── index.jsp                               # Trang gốc chuyển hướng sang /waiting
```

---

## 2. Các tính năng chính

1. **Đăng nhập bằng Session & Cookie**:
   - Khi người dùng đăng nhập thành công:
     - Tạo và lưu đối tượng `User` trong `HttpSession` (`account`, `username`).
     - Nếu tích chọn **"Ghi nhớ tôi (Lưu Cookie)"**: hệ thống tạo Cookie `username` lưu trên trình duyệt (`maxAge = 30 phút`).
   - Tự động nhận diện khi mở lại trình duyệt: Controller kiểm tra Cookie hợp lệ sẽ tự động khôi phục Session và chuyển thẳng vào hệ thống mà không cần đăng nhập lại.
2. **Đăng ký tài khoản (Register)**:
   - Kiểm tra rỗng và xác nhận mật khẩu.
   - Kiểm tra trùng lặp `username`, `email`, `phone` bằng JPQL truy vấn trong JPA.
   - Lưu tài khoản mới vào CSDL qua `em.persist(user)`.
3. **Phân quyền và điều hướng (WaitingController)**:
   - **Role 1 (Admin):** chuyển hướng sang `/admin/home` (Bảng điều khiển quản trị, hiển thị bảng danh sách tất cả tài khoản lấy từ CSDL qua JPA).
   - **Role 2 (Manager) & Role 3 (User):** chuyển hướng sang `/home`.
4. **Đăng xuất (Logout)**:
   - Hủy bỏ `HttpSession` (`session.invalidate()`).
   - Xóa bỏ Cookie ghi nhớ trên trình duyệt (`cookie.setMaxAge(0)`).
   - Chuyển hướng về trang `/login`.

---

## 3. Tài khoản thử nghiệm mặc định

Hệ thống tự động khởi tạo dữ liệu mẫu khi ứng dụng khởi chạy lần đầu:
| Tên đăng nhập | Mật khẩu | Quyền (Role) | Chuyển hướng sau đăng nhập |
|---------------|----------|--------------|---------------------------|
| `admin`       | `123456` | 1 - Admin    | `/admin/home`             |
| `manager`     | `123456` | 2 - Manager  | `/home`                   |
| `user`        | `123456` | 3 - User     | `/home`                   |

---

## 4. Hướng dẫn chạy dự án

### Cách 1: Chạy bằng file script PowerShell (Khuyên dùng)
1. Mở PowerShell tại thư mục dự án `24110050_PhanTheQuan_Week2`.
2. Chạy lệnh:
   ```powershell
   .\run-tomcat.ps1
   ```
3. Mở trình duyệt truy cập: [http://localhost:8080/Week2](http://localhost:8080/Week2)

### Cách 2: Chạy trong VS Code
- Nhấn tổ hợp phím `Ctrl + Shift + B` để chạy Task mặc định **🚀 Run Tomcat 11 (Week 2)**.

