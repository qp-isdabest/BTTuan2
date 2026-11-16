package vn.iotstar.util;

import org.mindrot.jbcrypt.BCrypt;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class PasswordUtil {

    /**
     * Mã hóa mật khẩu sử dụng thuật toán BCrypt với Salt ngẫu nhiên
     * @param plainPassword mật khẩu thô
     * @return chuỗi hash BCrypt an toàn
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống khi băm!");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    /**
     * Kiểm tra tính khớp giữa mật khẩu người dùng nhập và mật khẩu đã hash trong CSDL
     * @param plainPassword mật khẩu người dùng nhập vào
     * @param hashedPassword chuỗi hash trong CSDL
     * @return true nếu mật khẩu chính xác, false nếu sai
     */
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null) {
            return false;
        }

        // Kiểm tra nếu là chuỗi băm chuẩn BCrypt (bắt đầu bằng $2a$, $2b$ hoặc $2y$)
        if (hashedPassword.startsWith("$2a$") || hashedPassword.startsWith("$2b$") || hashedPassword.startsWith("$2y$")) {
            try {
                return BCrypt.checkpw(plainPassword, hashedPassword);
            } catch (Exception e) {
                System.err.println("Lỗi kiểm tra BCrypt hash: " + e.getMessage());
                return false;
            }
        }

        // Hỗ trợ tương thích ngược nếu CSDL cũ còn lưu plain-text
        return plainPassword.equals(hashedPassword);
    }

    /**
     * Phương thức băm mật khẩu bằng SHA-256 (hỗ trợ tham khảo thêm)
     * @param input chuỗi cần băm
     * @return chuỗi hex SHA-256
     */
    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Lỗi tính toán SHA-256: ", ex);
        }
    }
}
