package com.example.config.step;

import com.example.processing.Organization;
import com.example.processing.partitioner.OrganizationsResourcePartitioner;
import com.example.processing.processor.OrganizationProcessor;
import com.example.processing.reader.OrganizationReader;
import com.example.processing.writer.OrganizationWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import static com.example.ApplicationProperties.*;
import static com.example.ApplicationProperties.DATA_PROCESSING_DESTINATION_FOLDER_PATH;

@Configuration
public class ProcessingStepConfiguration {

    @Bean
    @StepScope
    public OrganizationReader organizationReader(@Value("#{stepExecutionContext['sourceFilePath']}") String sourceFilePath,
                                                 @Value("#{stepExecutionContext['linesToSkip']}") int linesToSkip,
                                                 @Value("#{stepExecutionContext['maxItemCount']}") int maxItemCount) {
        return new OrganizationReader(sourceFilePath, linesToSkip, maxItemCount);
    }

    @Bean
    @StepScope
    public OrganizationProcessor organizationProcessor() {
        return new OrganizationProcessor();
    }

    @Bean
    @StepScope
    public OrganizationWriter organizationWriter(@Value("#{stepExecutionContext['destinationFilePath']}") String destinationFilePath) {
        return new OrganizationWriter(destinationFilePath);
    }

    @Bean
    public Step processingStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager, OrganizationReader reader, OrganizationProcessor processor, OrganizationWriter writer) {
        return new StepBuilder("processingStep", jobRepository)
                .<Organization, Organization>chunk(DATA_PROCESSING_CHUNK_SIZE, platformTransactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Partitioner partitioner() {
        return new OrganizationsResourcePartitioner(DATA_PROCESSING_SOURCE_FILE_PATH, DATA_PROCESSING_DESTINATION_FOLDER_PATH);
    }

    @Bean
    public Step partitionedProcessingStep(JobRepository jobRepository, @Qualifier("processingStep") Step processingStep, TaskExecutor taskExecutor) {
        return new StepBuilder("partitionedProcessingStep", jobRepository)
                .partitioner("partitionedProcessingStep", partitioner())
                .gridSize(DATA_PROCESSING_PARTITIONS_NUMBER)
                .step(processingStep)
                .taskExecutor(taskExecutor)
                .build();
    }
}
