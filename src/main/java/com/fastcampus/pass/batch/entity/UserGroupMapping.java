package com.fastcampus.pass.batch.entity;

import com.fastcampus.pass.batch.entity.user.UserGroupMappingId;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "user_group_mapping")
@IdClass(UserGroupMappingId.class)
public class UserGroupMapping extends BaseEntity {

    @Id
    private String userGroupId;
    @Id
    private String userId;

    private String userGroupName;
    private String description;

    @Builder
    public UserGroupMapping(String userGroupId,
                            String userId,
                            String userGroupName,
                            String description) {
        this.userGroupId = userGroupId;
        this.userId = userId;
        this.userGroupName = userGroupName;
        this.description = description;
    }
}
