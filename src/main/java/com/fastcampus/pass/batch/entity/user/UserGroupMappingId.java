package com.fastcampus.pass.batch.entity.user;

import lombok.*;

import java.io.Serializable;

/**
 * UserGroupMapping 엔티티에서 @Id 가 2개 즉 PK 가 복합키로 들어가 있으므로
 * 따로 id 클래스를 선언 해줌
 */
@Getter
@ToString
@NoArgsConstructor
public class UserGroupMappingId implements Serializable {

    private String userGroupId;
    private String userId;

    @Builder
    public UserGroupMappingId(String userGroupId, String userId) {
        this.userGroupId = userGroupId;
        this.userId = userId;
    }
}
