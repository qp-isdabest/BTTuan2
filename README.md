# BÀI TẬP TUẦN 2 - LẬP TRÌNH WEB (WEBPR330479)
**Họ và tên:** Phan Thế Quân  
**MSSV:** 24110050  
**Lớp / Môn:** Lập Trình Web - ThS. Nguyễn Hữu Trung  
**Chủ đề:** Kiến trúc 3 tầng (Three-Tier Architecture), MVC Model 2, Đầy đủ CRUD bảng Videos (Mối quan hệ 1 - N với Users), Đăng nhập với Mật khẩu mã hóa (BCrypt Hash), Quên mật khẩu gửi OTP qua Email, Session & Cookie, Đăng ký kích hoạt OTP qua Email, Chức năng Đăng xuất, Công nghệ JPA (Jakarta Persistence 3.1 & Hibernate).

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
│       └── persistence.xml                     # Cấu hình Persistence Unit (UserPU) đăng ký User và Video
├── src/main/java/vn/iotstar/
│   ├── entity/
│   │   ├── User.java                           # Model User: 1 - N (@OneToMany videos)
│   │   └── Video.java                          # Model Video: N - 1 (@ManyToOne user)
│   ├── config/
│   │   ├── JpaConfig.java                      # Cấu hình khởi tạo EntityManagerFactory & EntityManager
│   │   └── AppInitializer.java                 # WebListener tự khởi tạo dữ liệu mẫu User & Video liên kết 1 - N
│   ├── dao/                                    # TẦNG 3: DATA ACCESS LAYER
│   │   ├── UserDao.java & UserDaoImpl.java     # DAO thao tác bảng users bằng JPA
│   │   └── VideoDao.java & VideoDaoImpl.java   # DAO CRUD bảng videos bằng JPA (insert, update, delete, findById, findAll, search...)
│   ├── service/                                # TẦNG 2: BUSINESS LOGIC LAYER
│   │   ├── UserService.java & UserServiceImpl  # Service nghiệp vụ người dùng, xác thực, OTP
│   │   └── VideoService.java & VideoServiceImpl# Service nghiệp vụ Video (CRUD logic)
│   ├── controller/                             # TẦNG 1: PRESENTATION LAYER (Controllers)
│   │   ├── ProfileController.java              # Quản lý Profile, Upload ảnh Cloudinary multipart, Xác thực đổi Email qua OTP (/profile, /verify-email-otp, /resend-email-otp)
│   │   ├── VideoController.java                # Xử lý CRUD Video & Phân trang 6sp/trang (/video, /videos, /video/add, /video/edit, /video/delete)
│   │   ├── LoginController.java                # Xử lý đăng nhập với BCrypt, kiểm tra OTP, lưu Session & Cookie
│   │   ├── RegisterController.java             # Xử lý đăng ký, băm mật khẩu, tạo mã OTP và gửi qua Email
│   │   ├── VerifyOtpController.java            # Tiếp nhận xác thực OTP (/verify-otp) và gửi lại OTP (/resend-otp)
│   │   ├── ForgotPasswordController.java       # Xử lý yêu cầu quên mật khẩu (/forgot-password)
│   │   ├── ResetPasswordController.java        # Xử lý xác nhận OTP và cập nhật mật khẩu mới (/reset-password)
│   │   ├── WaitingController.java              # Điều hướng người dùng sau khi đăng nhập dựa trên Vai trò (Role)
│   │   ├── HomeController.java                 # Trang chủ hiển thị 10 video mới nhất, thông tin Session, Cookie
│   │   ├── AdminHomeController.java            # Bảng điều khiển Admin xem toàn bộ User & Bảng Videos
│   │   └── LogoutController.java               # Hủy Session, xóa Cookie Remember-Me ở mọi Path
│   └── util/
│       ├── Constant.java                       # Hằng số định danh Session, Cookie, đường dẫn View
│       ├── CloudinaryUtil.java                 # Tiện ích cấu hình và Upload Multipart Image lên Cloudinary CDN
│       ├── ValidationUtil.java                 # Tiện ích kiểm tra tính hợp lệ dữ liệu (Email, Phone, Username, Password, URL, OTP)
│       ├── PasswordUtil.java                   # Tiện ích băm và xác thực mật khẩu BCrypt (kèm SHA-256)
│       └── EmailUtil.java                      # Tiện ích gửi Email HTML chứa mã OTP qua Jakarta Mail
└── src/main/webapp/
    ├── WEB-INF/
    │   ├── web.xml                             # Cấu hình Deployment Descriptor & SiteMesh 3 Filter
    │   ├── sitemesh3.xml                       # Tệp cấu hình phân định Decorator Mapping của SiteMesh 3
    │   ├── decorators/                         # Bộ giao diện mẫu Bootstrap 5 dùng chung (SiteMesh 3 Decorators)
    │   │   ├── web.jsp                         # Decorator tổng thể (<sitemesh:write property='body|head|title'/>)
    │   │   ├── header.jsp                      # Thanh điều hướng Navbar Bootstrap 5 responsive
    │   │   └── footer.jsp                      # Chân trang Bootstrap 5 chuyên nghiệp & thông tin sinh viên
    │   └── views/                              # TẦNG 1: PRESENTATION LAYER (Nội dung từng trang JSP)
    │       ├── profile.jsp                     # Giao diện Xem & Cập nhật Hồ sơ cá nhân (Form Multipart Cloudinary)
    │       ├── verify_email_otp.jsp            # Giao diện Nhập mã OTP xác thực khi đổi sang Email mới
    │       ├── topbar.jsp                      # Header / Thanh điều hướng dùng chung
    │       ├── video_list.jsp                  # Giao diện Danh sách Video + Tìm kiếm (READ)
    │       ├── video_form.jsp                  # Giao diện Form thêm mới & chỉnh sửa Video (CREATE / UPDATE)
    │       ├── login.jsp                       # Giao diện Đăng nhập (Remember Me, link Quên mật khẩu)
    │       ├── register.jsp                    # Giao diện Đăng ký tài khoản
    │       ├── verify_otp.jsp                  # Giao diện nhập mã OTP 6 số để kích hoạt tài khoản
    │       ├── forgot_password.jsp             # Giao diện yêu cầu khôi phục mật khẩu (nhập email/username)
    │       ├── reset_password.jsp              # Giao diện nhập OTP và thiết lập mật khẩu mới
    │       ├── home.jsp                        # Giao diện Trang chủ người dùng (hiển thị Video sở hữu của User)
    │       └── admin_home.jsp                  # Giao diện Trang quản trị Admin (hiển thị bảng Videos và Users)
    └── index.jsp                               # Trang gốc chuyển hướng sang /waiting
```

---

## 2. Chi tiết chức năng CRUD bảng Videos (JPA)

| Thao tác | URL / Phương thức | Mô tả & Cách thức hoạt động |
| :--- | :--- | :--- |
| **READ (Xem danh sách)** | `GET /videos` | Lấy danh sách toàn bộ video qua `VideoDaoImpl.findAll()`, nạp kèm thông tin người tạo qua `JOIN FETCH v.user`. Hỗ trợ tìm kiếm theo tiêu đề (`?keyword=...`). |
| **CREATE (Thêm video)** | `GET /video/add`<br>`POST /video/add` | Hiển thị form tạo mới và lưu vào CSDL qua `em.persist(video)`. Tự động gắn quyền sở hữu cho người dùng đang đăng nhập (User từ `HttpSession`). |
| **UPDATE (Chỉnh sửa)** | `GET /video/edit?id=...`<br>`POST /video/edit` | Nạp thông tin video hiện tại lên form theo `id`, cho phép chỉnh sửa tiêu đề, mô tả, poster, link và trạng thái kích hoạt qua `em.merge(video)`. |
| **DELETE (Xóa video)** | `GET /video/delete?id=...` | Tìm thực thể video theo `id` và xóa khỏi CSDL qua `em.remove(video)`. Có hộp thoại JavaScript xác nhận trước khi xóa. |

---

## 3. Cấu hình Sitemesh Decorator 3 với Template Bootstrap 5

Dự án đã tích hợp đầy đủ công nghệ **SiteMesh 3 Decorator** kết hợp **Bootstrap 5.3.3**:

### 3.1. Thư viện Maven (`pom.xml`)
Sử dụng SiteMesh phiên bản **3.3.0** hỗ trợ tương thích hoàn toàn chuẩn **Jakarta EE 10 / Jakarta Servlet 6.0** (Tomcat 11):
```xml
<dependency>
    <groupId>org.sitemesh</groupId>
    <artifactId>sitemesh</artifactId>
    <version>3.3.0</version>
</dependency>
```

### 3.2. Cấu hình Filter (`web.xml`)
Khai báo `ConfigurableSiteMeshFilter` đón bắt tất cả request và forward:
```xml
<filter>
    <filter-name>sitemesh</filter-name>
    <filter-class>org.sitemesh.config.ConfigurableSiteMeshFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>sitemesh</filter-name>
    <url-pattern>/*</url-pattern>
    <dispatcher>REQUEST</dispatcher>
    <dispatcher>FORWARD</dispatcher>
</filter-mapping>
```

### 3.3. Tệp định tuyến Decorator (`sitemesh3.xml`)
Định cấu hình decorator chính và loại trừ tài nguyên tĩnh:
```xml
<sitemesh>
    <mapping path="/*" decorator="/WEB-INF/decorators/web.jsp" />
    <mapping path="/static/*" exclude="true" />
    <mapping path="/assets/*" exclude="true" />
</sitemesh>
```

### 3.4. Cấu trúc Template Decorator Bootstrap (`WEB-INF/decorators/`)
* **`web.jsp`**: Khung sườn HTML5 chuẩn, tích hợp CDN Bootstrap 5.3.3 & Bootstrap Icons, sử dụng các thẻ `<sitemesh:write property='title'/>`, `<sitemesh:write property='head'/>`, `<sitemesh:write property='body'/>`.
* **`header.jsp`**: Thanh điều hướng Navbar Bootstrap với hiệu ứng responsive, menu phân quyền (Admin / Manager / User) và dropdown hồ sơ người dùng.
* **`footer.jsp`**: Chân trang Bootstrap 5 hiện đại (Sticky footer), hiển thị đầy đủ thông tin sinh viên (**Phan Thế Quân - MSSV: 24110050**) cùng các nhãn công nghệ Jakarta EE, Hibernate, SiteMesh 3.

---

## 4. Cơ chế Validation 2 lớp (Client-side & Server-side) cho các FORM

Dự án triển khai cơ chế kiểm tra tính hợp lệ dữ liệu toàn diện (Validation) 2 lớp an toàn:

### 4.1. Phía Trình duyệt (Client-side Validation)
* **Bootstrap 5 UI Validation (`needs-validation`, `novalidate`):** Tự động hiển thị các thông báo trực quan đỏ/xanh (`is-invalid`, `invalid-feedback`) ngay khi bấm submit hoặc nhập sai.
* **HTML5 Attributes:** Ràng buộc chặt chẽ với `required`, `minlength`, `maxlength`, `pattern`, `type="email"`, `type="url"`, `type="tel"`.
* **Custom JavaScript Rules:**
  - Kiểm tra mật khẩu xác nhận trùng khớp theo thời gian thực (`input` event trên `password` và `confirmPassword`).
  - Kiểm tra định dạng số điện thoại Việt Nam (10 số, bắt đầu bằng `03`, `05`, `07`, `08`, `09`).
  - Kiểm tra định dạng URL (bắt đầu bằng `http://` hoặc `https://`) cho Video và Poster.

### 4.2. Phía Máy chủ (Server-side Validation)
* **Tiện ích tập trung [ValidationUtil.java](file:///d:/Study/2026-2027/HK1/DOT2/WEB/BTTuan2/24110050_PhanTheQuan_Week2/src/main/java/vn/iotstar/util/ValidationUtil.java):** Xây dựng các hàm kiểm tra chuẩn hóa với Regular Expression cho Email, Phone, Username, Password, URL, OTP.
* **Các Form được bảo vệ toàn diện:**
  1. **Đăng nhập (Login - `LoginController` & `login.jsp`):** Kiểm tra bắt buộc không để trống, kiểm tra độ dài tối thiểu username, báo lỗi tài khoản không tồn tại, sai mật khẩu hoặc tài khoản chưa kích hoạt OTP. Giữ lại giá trị username.
  2. **Đăng ký (Register - `RegisterController` & `register.jsp`):** Kiểm tra các trường bắt buộc, định dạng username (3-30 ký tự, không dấu cách), định dạng họ và tên, định dạng Email RFC 5322, số điện thoại 10 số, độ dài mật khẩu (>= 6), mật khẩu xác nhận trùng khớp, kiểm tra trùng lặp trong CSDL. Giữ lại toàn bộ thông tin form khi phát hiện lỗi.
  3. **Quên mật khẩu (Forgot Password - `ForgotPasswordController` & `forgot_password.jsp`):** Kiểm tra bắt buộc, định dạng email hoặc username, kiểm tra sự tồn tại của tài khoản trước khi gửi OTP. Giữ lại giá trị input.
  4. **Đặt lại mật khẩu & Kích hoạt OTP (`ResetPasswordController`, `VerifyOtpController`):** Kiểm tra mã OTP đúng 6 chữ số (`^\d{6}$`), kiểm tra hạn 5 phút, kiểm tra mật khẩu mới >= 6 ký tự và trùng khớp mật khẩu xác nhận.
  5. **Thêm Video (Insert - `VideoController.processAdd` & `video_form.jsp`):** Kiểm tra tiêu đề bắt buộc (3 - 255 ký tự), mô tả (tối đa 2000 ký tự), kiểm tra định dạng Poster URL và Video URL (`https?://.+`). Giữ lại nguyên vẹn các trường đã nhập khi có lỗi.
  6. **Chỉnh sửa Video (Edit - `VideoController.processEdit` & `video_form.jsp`):** Kiểm tra `videoId` hợp lệ, tiêu đề (3 - 255 ký tự), format URL của Poster và Video, bảo toàn thông tin chỉnh sửa khi phát hiện lỗi dữ liệu.

---

## 5. Chức năng Quản lý Hồ sơ (Profile), Upload Ảnh Cloudinary & Xác thực Đổi Email qua OTP

Hệ thống cung cấp trang quản lý hồ sơ người dùng toàn diện tại URL `/profile` với giao diện Bootstrap được SiteMesh 3 tự động trang trí:

### 5.1. Cập nhật thông tin Fullname, Phone và Avatar
* **Xem thông tin chi tiết:** Hiển thị ảnh đại diện, họ và tên, username (chỉ đọc), email, số điện thoại, vai trò quyền hạn và ngày khởi tạo tài khoản.
* **Upload ảnh đại diện (Avatar) lên Cloudinary bằng Multipart:**
  - Servlet `ProfileController` được cấu hình `@MultipartConfig`.
  - Nhận file qua `request.getPart("image")`, kiểm tra định dạng hình ảnh hợp lệ (JPG, PNG, WEBP, GIF).
  - Tích hợp **Cloudinary Java SDK** (`com.cloudinary:cloudinary-http44:1.39.0`) tải ảnh trực tiếp lên Cloudinary CDN và lưu đường dẫn `secure_url` vào cơ sở dữ liệu.
  - Hỗ trợ JavaScript xem trước (Preview) ảnh đại diện ngay khi người dùng vừa chọn file.

### 5.2. Quy trình Bảo mật khi Đổi sang Địa chỉ Email Mới
* **Không đổi Email:** Nếu người dùng giữ nguyên email cũ và chỉ cập nhật họ tên, số điện thoại hoặc avatar, hệ thống cập nhật tức thì vào CSDL và Session.
* **Đổi sang Email mới:**
  1. Kiểm tra email mới không được trùng với bất kỳ tài khoản nào khác trong CSDL.
  2. Tự động sinh mã **OTP 6 chữ số** ngẫu nhiên với thời hạn hiệu lực **5 phút**.
  3. Gửi email chứa mã OTP xác thực đến chính **địa chỉ Email mới** qua tiện ích `EmailUtil.sendUpdateEmailOtp(...)`.
  4. Lưu trạng thái chờ cập nhật và chuyển hướng người dùng sang trang `/verify-email-otp`.
  5. Sau khi người dùng nhập chính xác mã OTP, hệ thống mới chính thức cập nhật địa chỉ email mới vào CSDL, cập nhật lại phiên làm việc `HttpSession` và thông báo hoàn tất thành công.

---

## 6. Các tính năng cốt lõi khác

1. **Mối quan hệ 1 - N (Users & Videos):** Bảng `videos` chứa khóa ngoại `user_id` tham chiếu tới khóa chính của bảng `users`.
2. **Quên mật khẩu gửi OTP qua Email:** Tự động sinh OTP 6 số (5 phút), gửi mail HTML, cập nhật mật khẩu băm BCrypt.
3. **Đăng nhập với mật khẩu mã hóa BCrypt:** Băm mật khẩu kèm Salt ngẫu nhiên, không lưu plain-text.
4. **Đăng xuất (Logout):** Xóa sạch session và cookie, bảo vệ toàn bộ các trang nội bộ.
5. **Đăng ký kích hoạt OTP:** Tài khoản mới mang `status = 0`, kích hoạt qua OTP mới được đăng nhập.
6. **Session & Cookie:** Ghi nhớ đăng nhập 30 phút, chuyển hướng thông minh theo vai trò Admin / User.

---

## 7. Tài khoản thử nghiệm mặc định

Mật khẩu ban đầu của các tài khoản mẫu đều là `123456`, đã được tự động băm BCrypt:
| Tên đăng nhập | Email | Mật khẩu thô | Quyền (Role) | Dữ liệu Video khởi tạo |
|---------------|-------|--------------|--------------|------------------------|
| `admin`       | `admin@iotstar.vn` | `123456` | 1 - Admin | 5 videos hệ thống |
| `manager`     | `manager@iotstar.vn` | `123456` | 2 - Manager | 3 videos quản lý |
| `user`        | `user@iotstar.vn` | `123456` | 3 - User | 8 videos (Phân trang 6sp/trang: Trang 1 có 6, Trang 2 có 2) |

---

## 8. Danh sách các URL chính của ứng dụng
- **Trang chủ (10 video mới nhất):** [http://localhost:8080/Week2/home](http://localhost:8080/Week2/home)
- **Hồ sơ cá nhân & Cập nhật Profile (Cloudinary):** [http://localhost:8080/Week2/profile](http://localhost:8080/Week2/profile)
- **Xác thực đổi Email mới (OTP Email):** [http://localhost:8080/Week2/verify-email-otp](http://localhost:8080/Week2/verify-email-otp)
- **Tất cả videos của user (Phân trang 6sp/trang):** [http://localhost:8080/Week2/video](http://localhost:8080/Week2/video)
- **Xem chi tiết và phát Video:** [http://localhost:8080/Week2/video/detail?id=...](http://localhost:8080/Week2/video/detail)
- **Đăng nhập (Session & Cookie):** [http://localhost:8080/Week2/login](http://localhost:8080/Week2/login)
- **Đăng ký (Kích hoạt OTP Email):** [http://localhost:8080/Week2/register](http://localhost:8080/Week2/register)
- **Quên mật khẩu (OTP Email):** [http://localhost:8080/Week2/forgot-password](http://localhost:8080/Week2/forgot-password)
- **Thêm video mới:** [http://localhost:8080/Week2/video/add](http://localhost:8080/Week2/video/add)
- **Bảng điều khiển Quản trị (Admin):** [http://localhost:8080/Week2/admin/home](http://localhost:8080/Week2/admin/home)

---

## 9. Hướng dẫn chạy dự án

### Cách 1: Chạy bằng file script PowerShell (Khuyên dùng)
1. Mở PowerShell tại thư mục dự án `24110050_PhanTheQuan_Week2`.
2. Chạy lệnh:
   ```powershell
   .\run-tomcat.ps1
   ```
3. Mở trình duyệt truy cập: [http://localhost:8080/Week2](http://localhost:8080/Week2)

### Cách 2: Chạy trong VS Code
- Nhấn tổ hợp phím `Ctrl + Shift + B` để chạy Task mặc định **🚀 Run Tomcat 11 (Week 2)**.
