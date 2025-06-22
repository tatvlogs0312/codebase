package org.example.codebase.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Schedule {

    @Scheduled(fixedRate = 10000)
    public void runUpdateRoomEndContract() {
        log.info("Run job");

    }
}
