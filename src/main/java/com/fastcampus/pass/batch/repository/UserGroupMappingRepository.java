package com.fastcampus.pass.batch.repository;

import com.fastcampus.pass.batch.entity.UserGroupMapping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGroupMappingRepository extends JpaRepository<UserGroupMapping, Integer> {

    /**
     * 그룹 id 를 받아와서 일치하는 UserGroupMapping 엔티티 조회
     * @param userGroupId
     * @return List<UserGroupMapping>
     */
    List<UserGroupMapping> findByUserGroupId(String userGroupId);
}
