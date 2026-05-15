package com.seatup.performance.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class PerformanceStatusScheduler {

    private final JobLauncher jobLauncher;
    private final Job openPerformanceJob;
    private final Job closePerformanceJob;

    public PerformanceStatusScheduler(
            JobLauncher jobLauncher,
            @Qualifier("openPerformanceJob") Job openPerformanceJob,
            @Qualifier("closePerformanceJob") Job closePerformanceJob
    ) {
        this.jobLauncher = jobLauncher;
        this.openPerformanceJob = openPerformanceJob;
        this.closePerformanceJob = closePerformanceJob;
    }

    @Scheduled(cron = "0 * * * * *")
    public void runOpenJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLocalDateTime("runAt", LocalDateTime.now())
                .toJobParameters();
        jobLauncher.run(openPerformanceJob, params);
    }

    @Scheduled(cron = "0 * * * * *")
    public void runCloseJob() throws Exception {
        JobParameters params = new JobParametersBuilder()
                .addLocalDateTime("runAt", LocalDateTime.now())
                .toJobParameters();
        jobLauncher.run(closePerformanceJob, params);
    }
}
