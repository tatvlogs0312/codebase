package org.example.codebase.schedule;

import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Schedule {

    @Scheduled(fixedRate = 10000)
    public void runJob() {
        log.info("Run job");
    }

    @Scheduled(cron = "0 0/5 * * * ?") // mỗi 5 phút
    @SchedulerLock(name = "generateReport", lockAtMostFor = "PT5M", lockAtLeastFor = "PT1M")
    public void generateReport() {
        System.out.println("Generating report...");
    }
}
