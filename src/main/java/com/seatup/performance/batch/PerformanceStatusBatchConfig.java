package com.seatup.performance.batch;

import com.seatup.performance.entity.Performance;
import com.seatup.performance.enums.PerformanceStatus;
import com.seatup.performance.repository.PerformanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class PerformanceStatusBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final PerformanceRepository performanceRepository;

    // OPEN JOB
    @Bean
    public Job openPerformanceJob() {
        return new JobBuilder("openPerformanceJob", jobRepository)
                .start(openPerformanceStep())
                .build();
    }

    @Bean
    public Step openPerformanceStep() {
        return new StepBuilder("openPerformanceStep", jobRepository)
                .<Performance, Performance>chunk(10, transactionManager)
                .reader(openPerformanceItemReader())
                .processor(openPerformanceItemProcessor())
                .writer(openPerformanceItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<Performance> openPerformanceItemReader() {
        return new ListItemReader<>(
                performanceRepository.findByStatusAndOpenDateTimeBefore(
                        PerformanceStatus.SCHEDULED, LocalDateTime.now()
                )
        );
    }

    @Bean
    public ItemProcessor<Performance, Performance> openPerformanceItemProcessor() {
        return performance -> {
            performance.open();
            return performance;
        };
    }

    @Bean
    public ItemWriter<Performance> openPerformanceItemWriter() {
        return chunk -> performanceRepository.saveAll(chunk.getItems());
    }

    // CLOSE JOB
    @Bean
    public Job closePerformanceJob() {
        return new JobBuilder("closePerformanceJob", jobRepository)
                .start(closePerformanceStep())
                .build();
    }

    @Bean
    public Step closePerformanceStep() {
        return new StepBuilder("closePerformanceStep", jobRepository)
                .<Performance, Performance>chunk(10, transactionManager)
                .reader(closePerformanceItemReader())
                .processor(closePerformanceItemProcessor())
                .writer(closePerformanceItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<Performance> closePerformanceItemReader() {
        return new ListItemReader<>(
                performanceRepository.findByStatusAndCloseDateTimeBefore(
                        PerformanceStatus.OPEN, LocalDateTime.now()
                )
        );
    }

    @Bean
    public ItemProcessor<Performance, Performance> closePerformanceItemProcessor() {
        return performance -> {
            performance.close();
            return performance;
        };
    }

    @Bean
    public ItemWriter<Performance> closePerformanceItemWriter() {
        return chunk -> performanceRepository.saveAll(chunk.getItems());
    }
}