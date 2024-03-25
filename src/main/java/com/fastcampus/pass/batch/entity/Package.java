package com.fastcampus.pass.batch.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@Table(name = "package")
@Entity
public class Package extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer packageSeq;

    private String packageName;
    private Integer count;
    private Integer period;

    @Builder
    public Package(Integer packageSeq,
                   String packageName,
                   Integer count,
                   Integer period) {
        this.packageSeq = packageSeq;
        this.packageName = packageName;
        this.count = count;
        this.period = period;
    }
}
