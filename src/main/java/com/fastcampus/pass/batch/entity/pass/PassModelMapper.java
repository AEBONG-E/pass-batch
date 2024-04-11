package com.fastcampus.pass.batch.entity.pass;

import com.fastcampus.pass.batch.entity.BulkPass;
import com.fastcampus.pass.batch.entity.Pass;
import com.fastcampus.pass.batch.entity.enums.BulkPassStatus;
import com.fastcampus.pass.batch.entity.enums.PassStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE) // 일치하지 않은 필드를 무시한다.
public interface PassModelMapper {

    PassModelMapper INSTANCE = Mappers.getMapper(PassModelMapper.class);

    // 필드명이 같지 않거나 custom 하게 매핑해주기 위해서 @Mapping 을 추가해주면 된다.
    @Mapping(target = "status", qualifiedByName = "defaultStatus")
    @Mapping(target = "remainingCount", source = "bulkPass.count")
    Pass toPass(BulkPass bulkPass, String userId);

    // BulkPassStatus 와 관계없이 PassStatus 값을 설정한다.
    @Named("defaultStatus")
    default PassStatus status(BulkPassStatus status) {
        return PassStatus.READY;
    }
}
