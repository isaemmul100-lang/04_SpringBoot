package com.bear.studyboard.post.entity;

import com.bear.studyboard.category.entity.Category;
import com.bear.studyboard.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "study_posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Column(name = "reference_url", length = 2048)
    private String referenceUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public StudyPost(Member author, Category category, String title, String body, String referenceUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.author = author;
        this.category = category;
        this.title = title;
        this.body = body;
        this.referenceUrl = referenceUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateProfile(String title, String body, String referenceUrl, LocalDateTime updatedAt) {
        if(title != null) {
            this.title = title;
        }
        if (body != null) {
            this.body = body;
        }
        if (referenceUrl != null) {
            this.referenceUrl = referenceUrl;
        }
        if (updatedAt != null) {
            this.updatedAt = updatedAt;
        }
    }
}
