package vn.iotstar.dao.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.dao.VideoDao;
import vn.iotstar.entity.Video;

import java.util.List;

public class VideoDaoImpl implements VideoDao {

    @Override
    public void insert(Video video) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(video);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Video video) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(video);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(int videoId) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Video video = em.find(Video.class, videoId);
            if (video != null) {
                em.remove(video);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Video findById(int videoId) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            return em.find(Video.class, videoId);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> findAll() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT v FROM Video v JOIN FETCH v.user ORDER BY v.videoId DESC";
            return em.createQuery(jpql, Video.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> findByUserId(int userId) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT v FROM Video v WHERE v.user.id = :userId ORDER BY v.videoId DESC";
            TypedQuery<Video> query = em.createQuery(jpql, Video.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> searchByTitle(String keyword) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT v FROM Video v JOIN FETCH v.user WHERE LOWER(v.title) LIKE LOWER(:keyword) ORDER BY v.videoId DESC";
            TypedQuery<Video> query = em.createQuery(jpql, Video.class);
            query.setParameter("keyword", "%" + keyword + "%");
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> findLatestVideos(int limit) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT v FROM Video v JOIN FETCH v.user WHERE v.active = true ORDER BY v.videoId DESC";
            return em.createQuery(jpql, Video.class)
                    .setMaxResults(limit)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> findAll(int page, int pageSize) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT v FROM Video v JOIN FETCH v.user ORDER BY v.videoId DESC";
            return em.createQuery(jpql, Video.class)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            em.close();
        }
    }

    @Override
    public long countAll() {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT COUNT(v) FROM Video v";
            return em.createQuery(jpql, Long.class).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> findByUserId(int userId, int page, int pageSize) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT v FROM Video v JOIN FETCH v.user WHERE v.user.id = :userId ORDER BY v.videoId DESC";
            return em.createQuery(jpql, Video.class)
                    .setParameter("userId", userId)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            em.close();
        }
    }

    @Override
    public long countByUserId(int userId) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT COUNT(v) FROM Video v WHERE v.user.id = :userId";
            return em.createQuery(jpql, Long.class)
                    .setParameter("userId", userId)
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> searchByTitle(String keyword, int page, int pageSize) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT v FROM Video v JOIN FETCH v.user WHERE LOWER(v.title) LIKE LOWER(:keyword) ORDER BY v.videoId DESC";
            return em.createQuery(jpql, Video.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            em.close();
        }
    }

    @Override
    public long countByTitle(String keyword) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT COUNT(v) FROM Video v WHERE LOWER(v.title) LIKE LOWER(:keyword)";
            return em.createQuery(jpql, Long.class)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> searchByTitleAndUserId(String keyword, int userId, int page, int pageSize) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT v FROM Video v JOIN FETCH v.user WHERE v.user.id = :userId AND LOWER(v.title) LIKE LOWER(:keyword) ORDER BY v.videoId DESC";
            return em.createQuery(jpql, Video.class)
                    .setParameter("userId", userId)
                    .setParameter("keyword", "%" + keyword + "%")
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            em.close();
        }
    }

    @Override
    public long countByTitleAndUserId(String keyword, int userId) {
        EntityManager em = JpaConfig.getEntityManager();
        try {
            String jpql = "SELECT COUNT(v) FROM Video v WHERE v.user.id = :userId AND LOWER(v.title) LIKE LOWER(:keyword)";
            return em.createQuery(jpql, Long.class)
                    .setParameter("userId", userId)
                    .setParameter("keyword", "%" + keyword + "%")
                    .getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        } finally {
            em.close();
        }
    }
}
