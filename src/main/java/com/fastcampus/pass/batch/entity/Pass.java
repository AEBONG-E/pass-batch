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

    /**
     * 이용권 만료에 따른 Pass 의 상태변경 및 만료일자 기록 메소드
     */
    public void expirePass() {
        this.status = PassStatus.EXPIRED;
        this.expiredAt = LocalDateTime.now();
    }

}
