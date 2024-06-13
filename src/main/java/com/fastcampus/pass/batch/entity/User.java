package com.fastcampus.pass.batch.entity;

import com.fastcampus.pass.batch.entity.enums.UserStatus;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Map;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "user")
@TypeDef(name = "json", typeClass = JsonType.class) // Json 의 타입 정의
public class User extends BaseEntity {

    @Id
    private String userId;
    private String userName;
    @Enumerated(EnumType.STRING)
    private UserStatus status;
    private String phone;

    // Json 형태로 저장되어 있는 문자열 데이터를 Map 으로 매핑
    @Type(type = "json")
    private Map<String, Object> meta;

    public String getUuid() {
        String uuid = null;
        if (meta.containsKey("uuid")) {
            uuid = String.valueOf(meta.get("uuid"));
        }
        return uuid;

    }

    @Builder
    public User(String userId,
                String userName,
                UserStatus status,
                String phone,
                Map<String, Object> meta) {
        this.userId = userId;
        this.userName = userName;
        this.status = status;
        this.phone = phone;
        this.meta = meta;
    }

}
