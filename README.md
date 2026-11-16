# BÀI TẬP TUẦN 2 - LẬP TRÌNH WEB (WEBPR330479)
**Họ và tên:** Phan Thế Quân  
**MSSV:** 24110050  
**Lớp / Môn:** Lập Trình Web - ThS. Nguyễn Hữu Trung  
**Chủ đề:** Kiến trúc 3 tầng (Three-Tier Architecture), MVC Model 2, Đăng nhập với Mật khẩu mã hóa (BCrypt Hash), Session & Cookie, Đăng ký kích hoạt OTP qua Email, Chức năng Đăng xuất, Công nghệ JPA (Jakarta Persistence 3.1 & Hibernate).

---

## 1. Cấu trúc dự án 3 Tầng (Three-Tier Architecture)

Dự án tuân thủ nghiêm ngặt mô hình chuẩn theo bài giảng slide:

```
24110050_PhanTheQuan_Week2/
├── pom.xml                                      # Cấu hình Maven (Jakarta Servlet 6.0, JPA 3.1, Hibernate 6, jBCrypt, Angus Mail)
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
│   │   └── AppInitializer.java                 # WebListener tự khởi tạo dữ liệu mẫu với mật khẩu băm BCrypt
│   ├── dao/                                    # TẦNG 3: DATA ACCESS LAYER
│   │   ├── UserDao.java                        # Interface DAO định nghĩa các thao tác CRUD với CSDL
│   │   └── impl/
│   │       └── UserDaoImpl.java                # Triển khai truy vấn dữ liệu bằng JPA (JPQL, EntityManager)
│   ├── service/                                # TẦNG 2: BUSINESS LOGIC LAYER
│   │   ├── UserService.java                    # Interface Service định nghĩa logic nghiệp vụ
│   │   └── impl/
│   │       └── UserServiceImpl.java            # Triển khai logic xác thực mật khẩu hash, tạo OTP, kiểm tra hạn OTP
│   ├── controller/                             # TẦNG 1: PRESENTATION LAYER (Controllers)
│   │   ├── LoginController.java                # Xử lý đăng nhập với BCrypt, kiểm tra OTP, lưu Session & Cookie
│   │   ├── RegisterController.java             # Xử lý đăng ký, băm mật khẩu, tạo mã OTP và gửi qua Email
│   │   ├── VerifyOtpController.java            # Tiếp nhận xác thực OTP (/verify-otp) và gửi lại OTP (/resend-otp)
│   │   ├── WaitingController.java              # Điều hướng người dùng sau khi đăng nhập dựa trên Vai trò (Role)
│   │   ├── HomeController.java                 # Trang chủ hiển thị thông tin Session & Cookie của User
│   │   ├── AdminHomeController.java            # Bảng điều khiển Admin xem toàn bộ danh sách User & Trạng thái OTP
│   │   └── LogoutController.java               # Hủy Session, xóa Cookie Remember-Me ở mọi Path
│   └── util/
│       ├── Constant.java                       # Hằng số định danh Session, Cookie, đường dẫn View
│       ├── PasswordUtil.java                   # Tiện ích băm và xác thực mật khẩu BCrypt (kèm SHA-256)
│       └── EmailUtil.java                      # Tiện ích gửi Email HTML chứa mã OTP qua Jakarta Mail
└── src/main/webapp/
    ├── WEB-INF/
    │   ├── web.xml                             # Deployment Descriptor
    │   └── views/                              # TẦNG 1: PRESENTATION LAYER (Views)
    │       ├── topbar.jsp                      # Header / Thanh điều hướng dùng chung
    │       ├── login.jsp                       # Giao diện Đăng nhập (hỗ trợ Remember Me, thông báo Logout)
    │       ├── register.jsp                    # Giao diện Đăng ký tài khoản
    │       ├── verify_otp.jsp                  # Giao diện nhập mã OTP 6 số để kích hoạt tài khoản
    │       ├── home.jsp                        # Giao diện Trang chủ người dùng (User & Manager)
    │       └── admin_home.jsp                  # Giao diện Trang quản trị Admin (hiển thị cột Trạng thái OTP)
    └── index.jsp                               # Trang gốc chuyển hướng sang /waiting
```

---

## 2. Các tính năng cốt lõi

### 1. Đăng nhập với mật khẩu được mã hóa (BCrypt Hash)
* Hệ thống sử dụng thư viện **jBCrypt** với thuật toán băm kèm muối ngẫu nhiên (`gensalt(12)`).
* Mật khẩu người dùng khi đăng ký hoặc khởi tạo mẫu không bao giờ lưu dưới dạng văn bản thô (plain-text) trong CSDL, mà luôn lưu chuỗi hash dạng `$2a$12$...`.
* Khi đăng nhập, hàm `PasswordUtil.checkPassword(plainPassword, hashedPassword)` so sánh mật khẩu người dùng nhập vào với chuỗi hash trong CSDL để đảm bảo an toàn tuyệt đối.

### 2. Chức năng Đăng xuất (Logout)
* Xử lý qua Servlet [LogoutController.java](file:///d:/Study/2026-2027/HK1/DOT2/WEB/BTTuan2/24110050_PhanTheQuan_Week2/src/main/java/vn/iotstar/controller/LogoutController.java) (`/logout`), hỗ trợ cả phương thức `GET` và `POST`.
* Hủy toàn bộ phiên làm việc của người dùng (`session.invalidate()`).
* Xóa sạch Cookie ghi nhớ đăng nhập `username` (`cookie.setMaxAge(0)`) trên cả context path và root path.
* Chuyển hướng người dùng về trang đăng nhập `/login?logout=1` và hiển thị thông báo: *"Bạn đã đăng xuất khỏi hệ thống thành công!"*.
* Sau khi đăng xuất, mọi nỗ lực truy cập vào các trang bảo vệ (`/home`, `/admin/home`) đều bị chặn và tự động đưa về `/login`.

### 3. Đăng ký & Kích hoạt qua OTP Email
* Khi đăng ký, mật khẩu được băm BCrypt và lưu tài khoản với trạng thái `status = 0`.
* Sinh mã OTP 6 chữ số có hiệu lực trong **5 phút** gửi tới email người dùng.
* Người dùng chỉ có thể đăng nhập sau khi xác thực OTP thành công trên trang `/verify-otp`.
* Có hỗ trợ nút **"Gửi lại mã OTP"** (`/resend-otp`) và chế độ Dev Mode hiển thị OTP để chấm bài tiện lợi.

### 4. Đăng nhập bằng Session & Cookie
* Quản lý trạng thái đăng nhập qua `HttpSession` (`account`, `username`).
* Checkbox **"Ghi nhớ tôi (Lưu Cookie)"** tạo Cookie `username` tồn tại trong 30 phút, giúp tự động nhận diện tài khoản khi truy cập lại.

---

## 3. Tài khoản thử nghiệm mặc định

Mật khẩu ban đầu của các tài khoản mẫu đều là `123456`, đã được tự động băm BCrypt khi nạp vào CSDL:
| Tên đăng nhập | Mật khẩu thô | Chuỗi lưu trong DB | Quyền (Role) | Trang sau đăng nhập |
|---------------|--------------|--------------------|--------------|---------------------|
| `admin`       | `123456`     | `$2a$12$...` (Hash)| 1 - Admin    | `/admin/home`       |
| `manager`     | `123456`     | `$2a$12$...` (Hash)| 2 - Manager  | `/home`             |
| `user`        | `123456`     | `$2a$12$...` (Hash)| 3 - User     | `/home`             |

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
