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
}
