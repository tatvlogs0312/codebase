package org.example.codebase.schedule;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Schedule {

    //@Scheduled(fixedRate = 10000)
    public void runJob() {
        log.info("Run job");
    }

    @Scheduled(fixedRate = 2000) // mỗi 2s chạy 1 lần
    @SchedulerLock(name = "generateReport", lockAtMostFor = "5m", lockAtLeastFor = "2s")
    public void generateReport() {
        //lockAtLeastFor => sau bao lâu chạy lại job
        //lockAtMostFor  => sau bao lâu sẽ mở khóa job nếu bị treo
        System.out.println("Generating report...");
    }
}
