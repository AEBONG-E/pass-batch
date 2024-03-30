package com.fastcampus.pass.batch.entity;

import com.fastcampus.pass.batch.entity.enums.BulkPassStatus;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "bulk_pass")
public class BulkPass extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bulkPassSeq;
    private Integer packageSeq;
    private String userGroupId;

    @Enumerated(EnumType.STRING)
    private BulkPassStatus status;
    private Integer count;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    @Builder
    public BulkPass(Integer bulkPassSeq,
                    Integer packageSeq,
                    String userGroupId,
                    BulkPassStatus status,
                    Integer count,
                    LocalDateTime startedAt,
                    LocalDateTime endedAt) {
        this.bulkPassSeq = bulkPassSeq;
        this.packageSeq = packageSeq;
        this.userGroupId = userGroupId;
        this.status = status;
        this.count = count;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

}
