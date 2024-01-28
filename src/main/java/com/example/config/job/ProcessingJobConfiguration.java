package com.example.config.job;

import com.example.processing.partitioner.OrganizationsResourcePartitioner;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static com.example.ApplicationProperties.*;

@Configuration
@EnableBatchProcessing
public class ProcessingJobConfiguration {

    @Bean
    public Partitioner partitioner() {
        return new OrganizationsResourcePartitioner(DATA_PROCESSING_SOURCE_FILE_PATH, DATA_PROCESSING_DESTINATION_FOLDER_PATH);
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(DATA_PROCESSING_THREAD_POOL_SIZE);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }

    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    public Step partitionedProcessingStep(JobRepository jobRepository, @Qualifier("processingStep") Step processingStep) {
        return new StepBuilder("partitionedProcessingStep", jobRepository)
                .partitioner("partitionedProcessingStep", partitioner())
                .gridSize(DATA_PROCESSING_PARTITIONS_NUMBER)
                .step(processingStep)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean("processingJob")
    public Job processingJob(JobRepository jobRepository, @Qualifier("partitionedProcessingStep") Step partitionedProcessingStep, @Qualifier("cleanupStep") Step cleanupStep) {
        return new JobBuilder("processingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(cleanupStep)
                .next(partitionedProcessingStep)
                .build();
    }
}
