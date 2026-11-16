package vn.iotstar.service;

import vn.iotstar.entity.Video;
import java.util.List;

public interface VideoService {

    void insert(Video video);

    void update(Video video);

    void delete(int videoId);

    Video findById(int videoId);

    List<Video> findAll();

    List<Video> findByUserId(int userId);

    List<Video> searchByTitle(String keyword);

    List<Video> findLatestVideos(int limit);

    List<Video> findTop10LatestVideos();
}
