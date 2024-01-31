package com.example.config.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static com.example.ApplicationProperties.TASK_EXECUTOR_THREAD_POOL_SIZE;

@Configuration
@EnableBatchProcessing
public class ProcessingJobConfiguration {

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(TASK_EXECUTOR_THREAD_POOL_SIZE);
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
    public Job processingJob(JobRepository jobRepository, @Qualifier("partitionedProcessingStep") Step partitionedProcessingStep, @Qualifier("cleanupStep") Step cleanupStep) {
        return new JobBuilder("processingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(cleanupStep)
                .next(partitionedProcessingStep)
                .build();
    }
}
