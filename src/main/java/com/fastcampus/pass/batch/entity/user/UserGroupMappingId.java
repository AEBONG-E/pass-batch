package com.fastcampus.pass.batch.entity.user;

import lombok.*;

import java.io.Serializable;

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
