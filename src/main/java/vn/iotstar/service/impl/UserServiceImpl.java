package vn.iotstar.service.impl;

import vn.iotstar.dao.UserDao;
import vn.iotstar.dao.impl.UserDaoImpl;
import vn.iotstar.entity.User;
import vn.iotstar.service.UserService;
import vn.iotstar.util.EmailUtil;

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
        if (user != null && password.equals(user.getPassWord())) {
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
        // roleid = 3 là người dùng thông thường, status = 1 (đã kích hoạt)
        User newUser = new User(email, username, fullname, password, null, 3, phone, date, 1, null, null);
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
        // status = 0 (Chưa kích hoạt, chờ nhập mã OTP)
        User newUser = new User(email, username, fullname, password, null, 3, phone, date, 0, otp, otpExpiry);
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
