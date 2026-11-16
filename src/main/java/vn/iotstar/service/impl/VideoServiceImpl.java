package vn.iotstar.service.impl;

import vn.iotstar.dao.VideoDao;
import vn.iotstar.dao.impl.VideoDaoImpl;
import vn.iotstar.entity.Video;
import vn.iotstar.service.VideoService;

import java.util.List;

public class VideoServiceImpl implements VideoService {

    private final VideoDao videoDao;

    public VideoServiceImpl() {
        this.videoDao = new VideoDaoImpl();
    }

    public VideoServiceImpl(VideoDao videoDao) {
        this.videoDao = videoDao;
    }

    @Override
    public void insert(Video video) {
        videoDao.insert(video);
    }

    @Override
    public void update(Video video) {
        videoDao.update(video);
    }

    @Override
    public void delete(int videoId) {
        videoDao.delete(videoId);
    }

    @Override
    public Video findById(int videoId) {
        return videoDao.findById(videoId);
    }

    @Override
    public List<Video> findAll() {
        return videoDao.findAll();
    }

    @Override
    public List<Video> findByUserId(int userId) {
        return videoDao.findByUserId(userId);
    }

    @Override
    public List<Video> searchByTitle(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return videoDao.findAll();
        }
        return videoDao.searchByTitle(keyword.trim());
    }

    @Override
    public List<Video> findLatestVideos(int limit) {
        return videoDao.findLatestVideos(limit > 0 ? limit : 10);
    }

    @Override
    public List<Video> findTop10LatestVideos() {
        return videoDao.findLatestVideos(10);
    }

    @Override
    public List<Video> findAll(int page, int pageSize) {
        int safePage = page > 0 ? page : 1;
        int safePageSize = pageSize > 0 ? pageSize : 6;
        return videoDao.findAll(safePage, safePageSize);
    }

    @Override
    public long countAll() {
        return videoDao.countAll();
    }

    @Override
    public List<Video> findByUserId(int userId, int page, int pageSize) {
        int safePage = page > 0 ? page : 1;
        int safePageSize = pageSize > 0 ? pageSize : 6;
        return videoDao.findByUserId(userId, safePage, safePageSize);
    }

    @Override
    public long countByUserId(int userId) {
        return videoDao.countByUserId(userId);
    }

    @Override
    public List<Video> searchByTitle(String keyword, int page, int pageSize) {
        int safePage = page > 0 ? page : 1;
        int safePageSize = pageSize > 0 ? pageSize : 6;
        if (keyword == null || keyword.trim().isEmpty()) {
            return videoDao.findAll(safePage, safePageSize);
        }
        return videoDao.searchByTitle(keyword.trim(), safePage, safePageSize);
    }

    @Override
    public long countByTitle(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return videoDao.countAll();
        }
        return videoDao.countByTitle(keyword.trim());
    }

    @Override
    public List<Video> searchByTitleAndUserId(String keyword, int userId, int page, int pageSize) {
        int safePage = page > 0 ? page : 1;
        int safePageSize = pageSize > 0 ? pageSize : 6;
        if (keyword == null || keyword.trim().isEmpty()) {
            return videoDao.findByUserId(userId, safePage, safePageSize);
        }
        return videoDao.searchByTitleAndUserId(keyword.trim(), userId, safePage, safePageSize);
    }

    @Override
    public long countByTitleAndUserId(String keyword, int userId) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return videoDao.countByUserId(userId);
        }
        return videoDao.countByTitleAndUserId(keyword.trim(), userId);
    }
}
