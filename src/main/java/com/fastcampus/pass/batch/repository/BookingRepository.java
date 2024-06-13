package com.fastcampus.pass.batch.repository;

import com.fastcampus.pass.batch.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
