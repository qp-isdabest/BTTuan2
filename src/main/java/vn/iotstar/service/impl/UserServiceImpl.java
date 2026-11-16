package vn.iotstar.service.impl;

import vn.iotstar.dao.UserDao;
import vn.iotstar.dao.impl.UserDaoImpl;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.util.EmailUtil;
import vn.iotstar.util.PasswordUtil;

import java.sql.Date;
import java.util.List;
import java.util.Random;

public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    public UserServiceImpl() {
        this.userDao = new UserDaoImpl();
    }

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User login(String username, String password) {
        if (username == null || password == null) {
            return null;
        }
        User user = this.get(username.trim());
        // Xác thực mật khẩu với chuỗi hash BCrypt trong CSDL
        if (user != null && PasswordUtil.checkPassword(password, user.getPassWord())) {
            return user;
        }
        return null;
    }

    @Override
    public User get(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        return userDao.get(username.trim());
    }

    @Override
    public User findByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        return userDao.findByEmail(email.trim());
    }

    @Override
    public User findByUsernameOrEmail(String identifier) {
        if (identifier == null || identifier.trim().isEmpty()) {
            return null;
        }
        String clean = identifier.trim();
        // Nếu chứa ký tự '@' thì ưu tiên tìm theo Email trước
        if (clean.contains("@")) {
            User user = userDao.findByEmail(clean);
            if (user != null) {
                return user;
            }
        }
        // Thử tìm theo Username
        User user = userDao.get(clean);
        if (user != null) {
            return user;
        }
        // Thử tìm theo Email nếu clean không có '@'
        return userDao.findByEmail(clean);
    }

    @Override
    public void insert(User user) {
        userDao.insert(user);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public boolean register(String username, String password, String email, String fullname, String phone) {
        if (userDao.checkExistUsername(username) || userDao.checkExistEmail(email)) {
            return false;
        }
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        // Băm mật khẩu bằng BCrypt trước khi lưu vào CSDL
        String hashedPassword = PasswordUtil.hashPassword(password.trim());
        User newUser = new User(email, username, fullname, hashedPassword, null, 3, phone, date, 1, null, null);
        userDao.insert(newUser);
        return true;
    }

    @Override
    public boolean registerWithOtp(String username, String password, String email, String fullname, String phone, String otp, long otpExpiry) {
        if (userDao.checkExistUsername(username) || userDao.checkExistEmail(email)) {
            return false;
        }
        long millis = System.currentTimeMillis();
        Date date = new Date(millis);
        // Băm mật khẩu bằng BCrypt trước khi lưu vào CSDL
        String hashedPassword = PasswordUtil.hashPassword(password.trim());
        User newUser = new User(email, username, fullname, hashedPassword, null, 3, phone, date, 0, otp, otpExpiry);
        userDao.insert(newUser);
        return true;
    }

    @Override
    public int verifyOtp(String email, String otp) {
        if (email == null || otp == null) {
            return -2;
        }
        User user = userDao.findByEmail(email.trim());
        if (user == null) {
            return -2; // Không tìm thấy người dùng
        }

        if (user.getStatus() == 1) {
            return 1; // Tài khoản đã được kích hoạt trước đó
        }

        // Kiểm tra thời gian hết hạn
        if (user.getOtpExpiry() != null && System.currentTimeMillis() > user.getOtpExpiry()) {
            return -1; // Mã OTP đã hết hạn
        }

        // Kiểm tra tính chính xác của OTP
        if (user.getOtp() != null && user.getOtp().equals(otp.trim())) {
            user.setStatus(1); // Kích hoạt tài khoản thành công
            user.setOtp(null);
            user.setOtpExpiry(null);
            userDao.update(user);
            return 1;
        }

        return 0; // Sai mã OTP
    }

    @Override
    public String resendOtp(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        User user = userDao.findByEmail(email.trim());
        if (user == null) {
            return null;
        }

        // Sinh mã OTP mới 6 số
        String newOtp = String.format("%06d", new Random().nextInt(1000000));
        long newExpiry = System.currentTimeMillis() + 5 * 60 * 1000; // 5 phút

        user.setOtp(newOtp);
        user.setOtpExpiry(newExpiry);
        userDao.update(user);

        // Gửi email chứa OTP
        EmailUtil.sendOtpEmail(user.getEmail(), user.getFullName(), newOtp);

        return newOtp;
    }

    @Override
    public String sendForgotPasswordOtp(String identifier) {
        User user = findByUsernameOrEmail(identifier);
        if (user == null) {
            return null;
        }

        // Sinh mã OTP 6 số
        String otp = String.format("%06d", new Random().nextInt(1000000));
        long expiry = System.currentTimeMillis() + 5 * 60 * 1000; // 5 phút

        user.setOtp(otp);
        user.setOtpExpiry(expiry);
        userDao.update(user);

        // Gửi email OTP khôi phục mật khẩu
        EmailUtil.sendForgotPasswordOtpEmail(user.getEmail(), user.getFullName(), otp);

        return otp;
    }

    @Override
    public int resetPassword(String email, String otp, String newPassword) {
        if (email == null || otp == null || newPassword == null || newPassword.trim().isEmpty()) {
            return -2;
        }
        User user = userDao.findByEmail(email.trim());
        if (user == null) {
            return -2; // Tài khoản không tồn tại
        }

        // Kiểm tra hết hạn OTP
        if (user.getOtpExpiry() != null && System.currentTimeMillis() > user.getOtpExpiry()) {
            return -1; // OTP đã hết hạn
        }

        // Kiểm tra mã OTP khớp
        if (user.getOtp() == null || !user.getOtp().equals(otp.trim())) {
            return 0; // Sai mã OTP
        }

        // Băm mật khẩu mới bằng BCrypt và cập nhật
        String hashedPassword = PasswordUtil.hashPassword(newPassword.trim());
        user.setPassWord(hashedPassword);
        user.setOtp(null);
        user.setOtpExpiry(null);
        user.setStatus(1); // Kích hoạt luôn nếu trước đó chưa kích hoạt
        userDao.update(user);

        return 1; // Thành công
    }

    @Override
    public boolean checkExistEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return userDao.checkExistEmail(email.trim());
    }

    @Override
    public boolean checkExistUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return userDao.checkExistUsername(username.trim());
    }

    @Override
    public boolean checkExistPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return userDao.checkExistPhone(phone.trim());
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }
}
