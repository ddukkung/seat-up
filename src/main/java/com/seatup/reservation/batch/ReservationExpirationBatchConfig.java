package com.seatup.reservation.batch;

import com.seatup.reservation.entity.Reservation;
import com.seatup.reservation.repository.ReservationRepository;
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
public class ReservationExpirationBatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final ReservationRepository reservationRepository;

    @Bean
    public Job expireReservationJob() {
        return new JobBuilder("expireReservationJob", jobRepository)
                .start(expireReservationStep())
                .build();
    }

    @Bean
    public Step expireReservationStep() {
        return new StepBuilder("expireReservationStep", jobRepository)
                .<Reservation, Reservation>chunk(10, transactionManager)
                .reader(reservationItemReader())
                .processor(reservationItemProcessor())
                .writer(reservationItemWriter())
                .listener(new ReservationItemErrorListener())
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<Reservation> reservationItemReader() {
        return new ListItemReader<>(reservationRepository.findExpiredReservations(LocalDateTime.now()));
    }

    @Bean
    public ItemProcessor<Reservation, Reservation> reservationItemProcessor() {
        return reservation -> {
            reservation.expire();
            return reservation;
        };
    }

    @Bean
    public ItemWriter<Reservation> reservationItemWriter() {
        return chunk -> reservationRepository.saveAll(chunk.getItems());
    }

}
