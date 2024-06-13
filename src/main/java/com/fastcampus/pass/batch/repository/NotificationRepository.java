package com.fastcampus.pass.batch.repository;

import com.fastcampus.pass.batch.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
