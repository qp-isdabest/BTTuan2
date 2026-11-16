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
}
