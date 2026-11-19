package vn.iotstar.util;

import java.net.URI;
import java.util.regex.Pattern;

public class ValidationUtil {

    // Regex kiểm tra định dạng Email chuẩn RFC 5322 cơ bản
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"
    );

    // Regex kiểm tra số điện thoại Việt Nam (10 chữ số, bắt đầu bằng 03, 05, 07, 08, 09)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^(03|05|07|08|09)\\d{8}$"
    );

    // Regex tên đăng nhập (3 - 30 ký tự, gồm chữ cái, chữ số và gạch dưới)
    private static final Pattern USERNAME_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_]{3,30}$"
    );

    // Regex mã OTP (đúng 6 chữ số)
    private static final Pattern OTP_PATTERN = Pattern.compile(
            "^\\d{6}$"
    );

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) return false;
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) return true; // Phone là trường tùy chọn, nếu để trống thì coi như hợp lệ
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidUsername(String username) {
        if (isEmpty(username)) return false;
        return USERNAME_PATTERN.matcher(username.trim()).matches();
    }

    public static boolean isValidPassword(String password) {
        if (isEmpty(password)) return false;
        return password.trim().length() >= 6;
    }

    public static boolean isValidOtp(String otp) {
        if (isEmpty(otp)) return false;
        return OTP_PATTERN.matcher(otp.trim()).matches();
    }

    public static boolean isValidUrl(String url) {
        if (isEmpty(url)) return true; // URL tùy chọn, nếu bỏ trống thì hợp lệ
        try {
            URI uri = URI.create(url.trim());
            String scheme = uri.getScheme();
            return scheme != null && (scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"));
        } catch (Exception e) {
            return false;
        }
    }
}
