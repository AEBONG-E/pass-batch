package com.fastcampus.pass.batch.repository;

import com.fastcampus.pass.batch.entity.Pass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassRepository extends JpaRepository<Pass, Integer> {

}
