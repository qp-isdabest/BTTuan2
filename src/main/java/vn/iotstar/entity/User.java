package vn.iotstar.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String userName;

    @Column(name = "fullname", length = 100, nullable = false)
    private String fullName;

    @Column(name = "password", length = 100, nullable = false)
    private String passWord;

    @Column(name = "avatar", length = 500)
    private String avatar;

    @Column(name = "roleid", nullable = false)
    private int roleid; // 1: Admin, 2: Manager, 3: User

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "status", nullable = false)
    private int status = 1; // 1: Đã kích hoạt, 0: Chưa kích hoạt (chờ OTP)

    @Column(name = "otp", length = 10)
    private String otp;

    @Column(name = "otp_expiry")
    private Long otpExpiry; // Thời gian hết hạn OTP (epoch milliseconds)

    // Quan hệ 1 - N: Một người dùng có thể sở hữu nhiều video
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Video> videos = new ArrayList<>();

    public User() {
    }

    public User(String email, String userName, String fullName, String passWord, String avatar, int roleid, String phone, Date createdDate) {
        this.email = email;
        this.userName = userName;
        this.fullName = fullName;
        this.passWord = passWord;
        this.avatar = avatar;
        this.roleid = roleid;
        this.phone = phone;
        this.createdDate = createdDate;
        this.status = 1;
    }

    public User(String email, String userName, String fullName, String passWord, String avatar, int roleid, String phone, Date createdDate, int status, String otp, Long otpExpiry) {
        this.email = email;
        this.userName = userName;
        this.fullName = fullName;
        this.passWord = passWord;
        this.avatar = avatar;
        this.roleid = roleid;
        this.phone = phone;
        this.createdDate = createdDate;
        this.status = status;
        this.otp = otp;
        this.otpExpiry = otpExpiry;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getRoleid() {
        return roleid;
    }

    public void setRoleid(int roleid) {
        this.roleid = roleid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Long getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(Long otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public void addVideo(Video video) {
        if (videos == null) {
            videos = new ArrayList<>();
        }
        videos.add(video);
        video.setUser(this);
    }

    public void removeVideo(Video video) {
        if (videos != null) {
            videos.remove(video);
            video.setUser(null);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", userName='" + userName + '\'' +
                ", fullName='" + fullName + '\'' +
                ", roleid=" + roleid +
                ", phone='" + phone + '\'' +
                ", createdDate=" + createdDate +
                ", status=" + status +
                '}';
    }
}
