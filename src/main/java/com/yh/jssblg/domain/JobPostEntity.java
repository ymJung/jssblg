package com.yh.jssblg.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

@Entity
@Data
public class JobPostEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: job_id, company_name, view_count, resume_count, created_at, updated_at
    // 필드 및 Getter/Setter 생성
    // 만약 created_at, updated_at 자동 관리 필요 시 @PrePersist, @PreUpdate 활용

    @Column(unique = true)
    private String jobId;

    private String companyName;
    private int viewCount;
    private int resumeCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private boolean postedToBlog;
    private String jobTitle;
    private String jobDescription;
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
