package vn.iotstar.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "videos")
public class Video implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "video_id")
    private int videoId;

    @Column(name = "title", length = 255, nullable = false)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "poster", length = 500)
    private String poster;

    @Column(name = "url", length = 500)
    private String url;

    @Column(name = "views")
    private int views = 0;

    @Column(name = "active")
    private boolean active = true;

    // Quan hệ N - 1: Nhiều video thuộc về một người dùng (User)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Video() {
    }

    public Video(String title, String description, String poster, String url, int views, boolean active, User user) {
        this.title = title;
        this.description = description;
        this.poster = poster;
        this.url = url;
        this.views = views;
        this.active = active;
        this.user = user;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Video{" +
                "videoId=" + videoId +
                ", title='" + title + '\'' +
                ", views=" + views +
                ", active=" + active +
                ", userId=" + (user != null ? user.getId() : null) +
                '}';
    }
}
