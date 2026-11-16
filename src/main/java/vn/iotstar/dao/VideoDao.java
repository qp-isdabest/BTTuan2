package vn.iotstar.dao;

import vn.iotstar.entity.Video;
import java.util.List;

public interface VideoDao {

    void insert(Video video);

    void update(Video video);

    void delete(int videoId);

    Video findById(int videoId);

    List<Video> findAll();

    List<Video> findByUserId(int userId);

    List<Video> searchByTitle(String keyword);

    List<Video> findLatestVideos(int limit);

    // Phân trang (Pagination)
    List<Video> findAll(int page, int pageSize);

    long countAll();

    List<Video> findByUserId(int userId, int page, int pageSize);

    long countByUserId(int userId);

    List<Video> searchByTitle(String keyword, int page, int pageSize);

    long countByTitle(String keyword);

    List<Video> searchByTitleAndUserId(String keyword, int userId, int page, int pageSize);

    long countByTitleAndUserId(String keyword, int userId);
}
