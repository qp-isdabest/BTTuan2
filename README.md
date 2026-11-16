# BÀI TẬP TUẦN 2 - LẬP TRÌNH WEB (WEBPR330479)
**Họ và tên:** Phan Thế Quân  
**MSSV:** 24110050  
**Lớp / Môn:** Lập Trình Web - ThS. Nguyễn Hữu Trung  
**Chủ đề:** Kiến trúc 3 tầng (Three-Tier Architecture), MVC Model 2, Đăng nhập với Session & Cookie, Đăng ký kích hoạt OTP qua Email, Công nghệ JPA (Jakarta Persistence 3.1 & Hibernate).

---

## 1. Cấu trúc dự án 3 Tầng (Three-Tier Architecture)

Dự án tuân thủ nghiêm ngặt mô hình chuẩn theo bài giảng slide:

```
24110050_PhanTheQuan_Week2/
├── pom.xml                                      # Cấu hình Maven (Jakarta Servlet 6.0, JPA 3.1, Hibernate 6, Angus Mail)
├── run-tomcat.ps1                              # Script 1-click build và khởi chạy Tomcat 11
├── .vscode/tasks.json                           # Task tích hợp trong VS Code
├── src/main/resources/
│   ├── email.properties                        # Cấu hình tài khoản gửi Email SMTP (Gmail)
│   └── META-INF/
│       └── persistence.xml                     # Cấu hình Persistence Unit (UserPU) kết nối CSDL qua JPA
├── src/main/java/vn/iotstar/
│   ├── entity/
│   │   └── User.java                           # Model / Entity ánh xạ CSDL qua JPA (@Entity, status, otp, otp_expiry)
│   ├── config/
│   │   ├── JpaConfig.java                      # Cấu hình khởi tạo EntityManagerFactory & EntityManager
│   │   └── AppInitializer.java                 # WebListener tự khởi tạo dữ liệu mẫu khi start web app
│   ├── dao/                                    # TẦNG 3: DATA ACCESS LAYER
│   │   ├── UserDao.java                        # Interface DAO định nghĩa các thao tác CRUD với CSDL
│   │   └── impl/
│   │       └── UserDaoImpl.java                # Triển khai truy vấn dữ liệu bằng JPA (JPQL, EntityManager)
│   ├── service/                                # TẦNG 2: BUSINESS LOGIC LAYER
│   │   ├── UserService.java                    # Interface Service định nghĩa logic nghiệp vụ (login, registerWithOtp, verifyOtp, resendOtp)
│   │   └── impl/
│   │       └── UserServiceImpl.java            # Triển khai logic xác thực, tạo OTP, kiểm tra hạn OTP
│   ├── controller/                             # TẦNG 1: PRESENTATION LAYER (Controllers)
│   │   ├── LoginController.java                # Xử lý đăng nhập, chặn tài khoản chưa kích hoạt, lưu Session & Cookie
│   │   ├── RegisterController.java             # Xử lý đăng ký, tạo mã OTP và gửi qua Email
│   │   ├── VerifyOtpController.java            # Tiếp nhận xác thực OTP (/verify-otp) và gửi lại OTP (/resend-otp)
│   │   ├── WaitingController.java              # Điều hướng người dùng sau khi đăng nhập dựa trên Vai trò (Role)
│   │   ├── HomeController.java                 # Trang chủ hiển thị thông tin Session & Cookie của User
│   │   ├── AdminHomeController.java            # Bảng điều khiển Admin xem toàn bộ danh sách User & Trạng thái OTP
│   │   └── LogoutController.java               # Hủy Session, xóa Cookie Remember-Me
│   └── util/
│       ├── Constant.java                       # Hằng số định danh Session, Cookie, đường dẫn View
│       └── EmailUtil.java                      # Tiện ích gửi Email HTML chứa mã OTP qua Jakarta Mail
└── src/main/webapp/
    ├── WEB-INF/
    │   ├── web.xml                             # Deployment Descriptor
    │   └── views/                              # TẦNG 1: PRESENTATION LAYER (Views)
    │       ├── topbar.jsp                      # Header / Thanh điều hướng dùng chung
    │       ├── login.jsp                       # Giao diện Đăng nhập (hỗ trợ Remember Me qua Cookie)
    │       ├── register.jsp                    # Giao diện Đăng ký tài khoản
    │       ├── verify_otp.jsp                  # Giao diện nhập mã OTP 6 số để kích hoạt tài khoản
    │       ├── home.jsp                        # Giao diện Trang chủ người dùng (User & Manager)
    │       └── admin_home.jsp                  # Giao diện Trang quản trị Admin (hiển thị cột Trạng thái OTP)
    └── index.jsp                               # Trang gốc chuyển hướng sang /waiting
```

---

## 2. Các tính năng chính

1. **Đăng ký tài khoản & Kích hoạt qua OTP Email**:
   - Khi đăng ký, hệ thống tự động sinh mã OTP ngẫu nhiên gồm 6 chữ số và thời hạn hiệu lực **5 phút**.
   - Tài khoản được lưu vào CSDL qua JPA với trạng thái `status = 0` (Chưa kích hoạt).
   - Hệ thống gửi email chứa mã OTP và chuyển hướng người dùng sang trang `/verify-otp`.
   - Người dùng nhập đúng mã OTP còn hiệu lực sẽ kích hoạt tài khoản (`status = 1`) và có thể đăng nhập.
   - Hỗ trợ chức năng **"Gửi lại mã OTP"** (`/resend-otp`).
   - Có chế độ hiển thị Dev Mode hint tiện lợi cho việc chấm bài / kiểm thử nội bộ không phụ thuộc SMTP mạng ngoài.

2. **Kiểm soát đăng nhập**:
   - Tài khoản chưa kích hoạt (`status = 0`) khi đăng nhập sẽ bị từ chối với thông báo rõ ràng kèm nút điều hướng đến trang nhập OTP.
   - Đăng nhập thành công sẽ lưu phiên `HttpSession` (`account`, `username`).
   - Tùy chọn **"Ghi nhớ tôi (Lưu Cookie)"** tạo Cookie `username` lưu trên trình duyệt (`maxAge = 30 phút`).
   - Tự động nhận diện khi mở lại trình duyệt nếu đã lưu Cookie.

3. **Phân quyền và điều hướng (WaitingController)**:
   - **Role 1 (Admin):** chuyển hướng sang `/admin/home` (xem danh sách người dùng, vai trò và trạng thái kích hoạt OTP).
   - **Role 2 (Manager) & Role 3 (User):** chuyển hướng sang `/home`.

4. **Đăng xuất (Logout)**:
   - Hủy bỏ `HttpSession` (`session.invalidate()`).
   - Xóa bỏ Cookie ghi nhớ trên trình duyệt (`cookie.setMaxAge(0)`).
   - Chuyển hướng về trang `/login`.

---

## 3. Cấu hình gửi Email thực tế (Tùy chọn)

Để gửi email thật qua tài khoản Gmail cá nhân, mở file `src/main/resources/email.properties` và điền thông tin:
```properties
mail.sender=your_email@gmail.com
mail.password=your_16_character_app_password
```
*(Nếu giữ nguyên mật khẩu mặc định, hệ thống sẽ in mã OTP ra console máy chủ và hiển thị trên giao diện ở chế độ Dev Mode để kiểm thử ngay lập tức mà không cần tạo App Password).*

---

## 4. Tài khoản thử nghiệm mặc định

Hệ thống tự động khởi tạo dữ liệu mẫu khi ứng dụng khởi chạy lần đầu:
| Tên đăng nhập | Mật khẩu | Quyền (Role) | Trạng thái OTP | Trang sau khi đăng nhập |
|---------------|----------|--------------|----------------|-------------------------|
| `admin`       | `123456` | 1 - Admin    | Đã kích hoạt   | `/admin/home`           |
| `manager`     | `123456` | 2 - Manager  | Đã kích hoạt   | `/home`                 |
| `user`        | `123456` | 3 - User     | Đã kích hoạt   | `/home`                 |

---

## 5. Hướng dẫn chạy dự án

### Cách 1: Chạy bằng file script PowerShell (Khuyên dùng)
1. Mở PowerShell tại thư mục dự án `24110050_PhanTheQuan_Week2`.
2. Chạy lệnh:
   ```powershell
   .\run-tomcat.ps1
   ```
3. Mở trình duyệt truy cập: [http://localhost:8080/Week2](http://localhost:8080/Week2)

### Cách 2: Chạy trong VS Code
- Nhấn tổ hợp phím `Ctrl + Shift + B` để chạy Task mặc định **🚀 Run Tomcat 11 (Week 2)**.
