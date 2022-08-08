package com.mealfit.common.baseEntity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@MappedSuperclass // Entity가 자동으로 컬럼으로 인식합니다.
@EntityListeners(AuditingEntityListener.class) // 생성/변경 시간을 자동으로 업데이트합니다.
public class BaseEntity {

    @Column(updatable = false) @CreatedDate
    private LocalDateTime createdAt;

    @Column(updatable = false) @CreatedBy
    private Long createdBy;

    @Column(insertable = false) @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(insertable = false) @LastModifiedBy
    private Long updatedBy;
}
