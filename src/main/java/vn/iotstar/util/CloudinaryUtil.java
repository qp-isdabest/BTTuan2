package vn.iotstar.util;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.http.Part;

import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

public class CloudinaryUtil {

    private static Cloudinary cloudinary;
    private static String cloudName = "dckj0j4w5";
    private static String apiKey = "428989524675747";
    private static String apiSecret = "7g1Fj1u_VqZ72xLq74x4g4oG1Q0";

    static {
        try (InputStream is = CloudinaryUtil.class.getClassLoader().getResourceAsStream("cloudinary.properties")) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                cloudName = props.getProperty("cloudinary.cloud_name", cloudName);
                apiKey = props.getProperty("cloudinary.api_key", apiKey);
                apiSecret = props.getProperty("cloudinary.api_secret", apiSecret);
            }
        } catch (Exception e) {
            System.err.println("Could not load cloudinary.properties: " + e.getMessage());
        }

        // Khởi tạo đối tượng Cloudinary
        cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret,
                "secure", true
        ));
    }

    public static Cloudinary getCloudinary() {
        return cloudinary;
    }

    /**
     * Upload file Part từ Servlet Multipart lên Cloudinary và trả về đường dẫn URL an toàn (secure_url).
     *
     * @param filePart đối tượng Part nhận được từ HttpServletRequest.getPart("image")
     * @return URL ảnh trên Cloudinary hoặc null nếu không có file hợp lệ
     * @throws Exception nếu có lỗi trong quá trình upload
     */
    public static String uploadImage(Part filePart) throws Exception {
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }

        String submittedFileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        if (submittedFileName == null || submittedFileName.trim().isEmpty()) {
            return null;
        }

        // Đọc toàn bộ byte dữ liệu từ InputStream của Part
        byte[] fileBytes;
        try (InputStream input = filePart.getInputStream()) {
            fileBytes = input.readAllBytes();
        }

        if (fileBytes.length == 0) {
            return null;
        }

        // Upload trực tiếp mảng byte lên Cloudinary
        @SuppressWarnings("unchecked")
        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                fileBytes,
                ObjectUtils.asMap(
                        "folder", "week2_avatars",
                        "resource_type", "image"
                )
        );

        if (uploadResult != null && uploadResult.containsKey("secure_url")) {
            String secureUrl = (String) uploadResult.get("secure_url");
            System.out.println(">>> [CLOUDINARY SUCCESS] Đã upload ảnh lên Cloudinary: " + secureUrl);
            return secureUrl;
        }

        return null;
    }
}
