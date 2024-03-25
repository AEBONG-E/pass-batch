package com.fastcampus.pass.batch.entity;

import com.fastcampus.pass.batch.entity.enums.PassStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@Table(name = "pass")
@Entity
public class Pass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer passSeq;
    private Integer packageSeq;
    private String userId;

    @Enumerated(EnumType.STRING)
    private PassStatus status;
    private Integer remainingCount;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime expiredAt;

    @Builder
    public Pass(Integer passSeq,
                Integer packageSeq,
                String userId,
                PassStatus status,
                Integer remainingCount,
                LocalDateTime startedAt,
                LocalDateTime endedAt,
                LocalDateTime expiredAt) {
        this.passSeq = passSeq;
        this.packageSeq = packageSeq;
        this.userId = userId;
        this.status = status;
        this.remainingCount = remainingCount;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.expiredAt = expiredAt;
    }
}
