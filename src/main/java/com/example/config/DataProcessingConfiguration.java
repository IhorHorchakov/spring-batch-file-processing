package com.example.config;

import com.example.processing.Organization;
import com.example.processing.partitioner.OrganizationsResourcePartitioner;
import com.example.processing.processor.OrganizationProcessor;
import com.example.processing.reader.OrganizationReader;
import com.example.processing.tasklet.FileDeletingTasklet;
import com.example.processing.writer.OrganizationWriter;
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
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import static com.example.ApplicationProperties.*;

@Configuration
@EnableBatchProcessing
public class DataProcessingConfiguration {

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
    public Tasklet fileDeletingTasklet() {
        return new FileDeletingTasklet(DATA_PROCESSING_DESTINATION_FOLDER_PATH);
    }


    @Bean("fileDeletingStep")
    public Step fileDeletingStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("fileDeletingStep", jobRepository)
                .tasklet(fileDeletingTasklet(), transactionManager)
                .build();
    }

    @Bean("processingStep")
    public Step processingStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, OrganizationReader reader, OrganizationProcessor processor, OrganizationWriter writer) {
        return new StepBuilder("processingStep", jobRepository)
                .<Organization, Organization>chunk(DATA_PROCESSING_CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean("partitionedProcessingStep")
    public Step mainStep(JobRepository jobRepository, @Qualifier("processingStep") Step processingStep) {
        return new StepBuilder("partitionedProcessingStep", jobRepository)
                .partitioner("partitionedProcessingStep", partitioner())
                .gridSize(DATA_PROCESSING_PARTITIONS_NUMBER)
                .step(processingStep)
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean("processingJob")
    public Job processingJob(JobRepository jobRepository, @Qualifier("partitionedProcessingStep") Step partitionedProcessingStep, @Qualifier("fileDeletingStep") Step fileDeletingStep) {
        return new JobBuilder("processingJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(fileDeletingStep)
                .next(partitionedProcessingStep)
                .build();
    }
}
