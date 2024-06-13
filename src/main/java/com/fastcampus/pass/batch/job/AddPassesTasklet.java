package com.fastcampus.pass.batch.job;

import com.fastcampus.pass.batch.entity.BulkPass;
import com.fastcampus.pass.batch.entity.Pass;
import com.fastcampus.pass.batch.entity.UserGroupMapping;
import com.fastcampus.pass.batch.entity.enums.BulkPassStatus;
import com.fastcampus.pass.batch.entity.mapper.PassModelMapper;
import com.fastcampus.pass.batch.repository.BulkPassRepository;
import com.fastcampus.pass.batch.repository.PassRepository;
import com.fastcampus.pass.batch.repository.UserGroupMappingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class AddPassesTasklet implements Tasklet {

    private final PassRepository passRepository;
    private final BulkPassRepository bulkPassRepository;
    private final UserGroupMappingRepository userGroupMappingRepository;

    public AddPassesTasklet(PassRepository passRepository,
                            BulkPassRepository bulkPassRepository,
                            UserGroupMappingRepository userGroupMappingRepository) {
        this.passRepository = passRepository;
        this.bulkPassRepository = bulkPassRepository;
        this.userGroupMappingRepository = userGroupMappingRepository;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution,
                                ChunkContext chunkContext) throws Exception {

        // 이용권 시작 일시 1일전 user group 내 각 사용자에게 이용권을 추가해준다.
        final LocalDateTime startedAt = LocalDateTime.now().minusDays(1);
        // 대량 이용권 상태가 아직 처리가 안됬을 때
        final List<BulkPass> bulkPassList = bulkPassRepository.findByStatusAndStartedAtGreaterThan(BulkPassStatus.READY, startedAt);
        int count = 0;

        // 대량 이용권 정보를 돌면서 user group 에 속한 userId 를 조회하고 해당 userId 로 이용권을 추가한다.
        for (BulkPass bulkPass: bulkPassList) {
            final List<String> userIdList = userGroupMappingRepository.findByUserGroupId(bulkPass.getUserGroupId())
                    .stream().map(UserGroupMapping::getUserId).toList();
            count += addPasses(bulkPass, userIdList);

            bulkPass.updateStatusByBatch();
        }

        log.info("AddPassesTasklet - execute: 이용권 {}건 추가완료, startedAt={}", count, startedAt);

        return RepeatStatus.FINISHED;
    }

    /**
     * bulkPass 의 정보로 mapper 데이터를 생성한다.
     * @param bulkPass
     * @param userIdList
     * @return int
     */
    private int addPasses(BulkPass bulkPass, List<String> userIdList) {
        List<Pass> passList = new ArrayList<>();
        for (String userId: userIdList) {
            Pass pass = PassModelMapper.INSTANCE.toPass(bulkPass, userId);
            passList.add(pass);
        }
        return passRepository.saveAll(passList).size();
    }

}
