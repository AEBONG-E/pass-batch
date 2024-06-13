package com.fastcampus.pass.batch.entity.mapper;

import com.fastcampus.pass.batch.entity.Booking;
import com.fastcampus.pass.batch.entity.Notification;
import com.fastcampus.pass.batch.entity.enums.NotificationEvent;
import com.fastcampus.pass.batch.util.LocalDateTimeUtils;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

// ReportingPolicy.IGNORE: 일치하지 않는 필드를 무시한다.
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, builder = @Builder(disableBuilder = false))
public interface NotificationModelMapper {

    NotificationModelMapper INSTANCE = Mappers.getMapper(NotificationModelMapper.class);

    // 필드명이 같지 않거나 custom 하게 매핑해주기 위해서 @Mapping 어노테이션을 추가 해준다.
    @Mapping(target = "uuid", source = "booking.user.uuid")
    @Mapping(target = "text", source = "booking.startedAt", qualifiedByName = "text")
    Notification toNotification(Booking booking, NotificationEvent event);

    // 알람 보낼 메시지 생성
    @Named("text")
    default String text(LocalDateTime startedAt) {
        return String.format("안녕하세요. %s 수업 시작합니다. 수업 전 출석 체크 부탁드립니다. \uD83D\uDE0A", LocalDateTimeUtils.format(startedAt));
    }

}
