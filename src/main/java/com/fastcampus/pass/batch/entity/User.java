package com.fastcampus.pass.batch.entity;

import com.fastcampus.pass.batch.entity.enums.UserStatus;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    private String userId;
    private String userName;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    private String phone;
    private String meta;

    @Builder
    public User(String userId,
                String userName,
                UserStatus status,
                String phone,
                String meta) {
        this.userId = userId;
        this.userName = userName;
        this.status = status;
        this.phone = phone;
        this.meta = meta;
    }

}
