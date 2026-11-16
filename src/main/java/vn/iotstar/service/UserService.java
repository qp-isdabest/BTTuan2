package vn.iotstar.service;

import vn.iotstar.entity.User;
import java.util.List;

public interface UserService {

    User login(String username, String password);

    User get(String username);

    User findByEmail(String email);

    User findByUsernameOrEmail(String identifier);

    void insert(User user);

    void update(User user);

    boolean register(String username, String password, String email, String fullname, String phone);

    boolean registerWithOtp(String username, String password, String email, String fullname, String phone, String otp, long otpExpiry);

    int verifyOtp(String email, String otp); // 1: Thành công, 0: Sai mã OTP, -1: OTP đã hết hạn, -2: Không tìm thấy user

    String resendOtp(String email);

    String sendForgotPasswordOtp(String identifier); // Trả về OTP nếu thành công, null nếu không tìm thấy tài khoản

    int resetPassword(String email, String otp, String newPassword); // 1: Thành công, 0: Sai OTP, -1: Hết hạn, -2: Không tìm thấy

    boolean checkExistEmail(String email);

    boolean checkExistUsername(String username);

    boolean checkExistPhone(String phone);

    List<User> findAll();
}
