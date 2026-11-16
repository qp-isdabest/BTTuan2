package vn.iotstar.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class EmailUtil {

    private static final Properties props = new Properties();
    private static String senderEmail = "example.test.iotstar@gmail.com";
    private static String senderPassword = "your_app_password_here";

    static {
        try (InputStream is = EmailUtil.class.getClassLoader().getResourceAsStream("email.properties")) {
            if (is != null) {
                props.load(is);
                senderEmail = props.getProperty("mail.sender", senderEmail);
                senderPassword = props.getProperty("mail.password", senderPassword);
            }
        } catch (Exception e) {
            System.err.println("Could not load email.properties: " + e.getMessage());
        }
    }

    /**
     * Gửi email chứa mã OTP kích hoạt tài khoản
     */
    public static boolean sendOtpEmail(String recipientEmail, String recipientName, String otpCode) {
        return sendCustomOtpEmail(recipientEmail, recipientName, otpCode, 
                "Mã OTP Kích Hoạt Tài Khoản Của Bạn", 
                "KÍCH HOẠT TÀI KHOẢN", 
                "Cảm ơn bạn đã đăng ký tài khoản. Để hoàn tất kích hoạt, vui lòng nhập mã OTP xác thực dưới đây vào trang đăng ký:");
    }

    /**
     * Gửi email chứa mã OTP khôi phục/đặt lại mật khẩu
     */
    public static boolean sendForgotPasswordOtpEmail(String recipientEmail, String recipientName, String otpCode) {
        return sendCustomOtpEmail(recipientEmail, recipientName, otpCode, 
                "Mã OTP Đặt Lại Mật Khẩu Của Bạn", 
                "ĐẶT LẠI MẬT KHẨU", 
                "Chúng tôi nhận được yêu cầu đặt lại mật khẩu cho tài khoản của bạn. Vui lòng nhập mã OTP dưới đây để hoàn tất việc tạo mật khẩu mới:");
    }

    private static boolean sendCustomOtpEmail(String recipientEmail, String recipientName, String otpCode, String subject, String headerTitle, String description) {
        System.out.println("\n=======================================================");
        System.out.println(">>> [OTP EMAIL SERVICE - " + headerTitle + "]");
        System.out.println(">>> Gửi OTP tới email: " + recipientEmail);
        System.out.println(">>> Họ tên người nhận: " + recipientName);
        System.out.println(">>> MÃ OTP XÁC THỰC   : [" + otpCode + "]");
        System.out.println("=======================================================\n");

        if (senderPassword == null || senderPassword.contains("your_app_password") || senderPassword.trim().isEmpty()) {
            System.out.println(">>> [INFO] Chưa thiết lập mật khẩu SMTP thực tế trong email.properties. Hệ thống in mã OTP ra console để kiểm thử.");
            return false;
        }

        try {
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail, "Hệ Thống Xác Thực Web", StandardCharsets.UTF_8.name()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject, StandardCharsets.UTF_8.name());

            String htmlBody = "<div style=\"font-family: Arial, sans-serif; max-width: 580px; margin: auto; padding: 25px; border: 1px solid #e2e8f0; border-radius: 12px; background-color: #ffffff;\">"
                    + "<div style=\"text-align: center; border-bottom: 2px solid #2563eb; padding-bottom: 15px;\">"
                    + "<h2 style=\"color: #2563eb; margin: 0;\">" + headerTitle + "</h2>"
                    + "<p style=\"color: #64748b; margin-top: 5px; font-size: 14px;\">Hệ thống Lập trình Web - JPA Three-Tier</p>"
                    + "</div>"
                    + "<div style=\"padding: 20px 0;\">"
                    + "<p>Xin chào <strong>" + (recipientName != null ? recipientName : "Bạn") + "</strong>,</p>"
                    + "<p>" + description + "</p>"
                    + "<div style=\"text-align: center; margin: 25px 0;\">"
                    + "<span style=\"display: inline-block; font-size: 32px; font-weight: 800; letter-spacing: 8px; color: #1e40af; background: #eff6ff; padding: 12px 28px; border-radius: 8px; border: 1px dashed #3b82f6;\">"
                    + otpCode
                    + "</span>"
                    + "</div>"
                    + "<p style=\"color: #dc2626; font-size: 13px;\"><i style=\"font-size: 15px;\">⚠️</i> <strong>Lưu ý:</strong> Mã OTP có hiệu lực trong vòng <strong>5 phút</strong>. Tuyệt đối không chia sẻ mã này cho bất kỳ ai khác.</p>"
                    + "</div>"
                    + "<div style=\"border-top: 1px solid #e2e8f0; padding-top: 15px; font-size: 12px; color: #94a3b8; text-align: center;\">"
                    + "<p style=\"margin: 0;\">Nếu bạn không yêu cầu hành động này, vui lòng bỏ qua thư này hoặc liên hệ hỗ trợ.</p>"
                    + "</div>"
                    + "</div>";

            message.setContent(htmlBody, "text/html; charset=UTF-8");

            Transport.send(message);
            System.out.println(">>> [SUCCESS] Đã gửi email OTP thực tế tới " + recipientEmail + " thành công!");
            return true;
        } catch (Exception e) {
            System.err.println(">>> [ERROR] Gửi email SMTP thất bại: " + e.getMessage());
            return false;
        }
    }
}
