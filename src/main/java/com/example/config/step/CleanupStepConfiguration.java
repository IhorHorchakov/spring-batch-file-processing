package com.example.config.step;

import com.example.processing.tasklet.FileDeletingTasklet;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import static com.example.ApplicationProperties.DATA_PROCESSING_DESTINATION_FOLDER_PATH;

@Configuration
public class CleanupStepConfiguration {

    @Bean
    public Tasklet fileDeletingTasklet() {
        return new FileDeletingTasklet(DATA_PROCESSING_DESTINATION_FOLDER_PATH);
    }


    @Bean
    public Step cleanupStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("cleanupStep", jobRepository)
                .tasklet(fileDeletingTasklet(), transactionManager)
                .build();
    }

}
