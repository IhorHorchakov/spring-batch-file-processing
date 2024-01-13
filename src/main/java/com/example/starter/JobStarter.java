package com.example.starter;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JobStarter {
    @Autowired
    @Qualifier("processingJob")
    private Job job;
    @Autowired
    private JobLauncher jobLauncher;

    @PostConstruct
    public void startProcessingJob() {
        log.info("Preparing a job to launch");
        JobParameters params = new JobParametersBuilder().toJobParameters();
        try {
            jobLauncher.run(job, params);
        } catch (JobExecutionException e) {
            throw new RuntimeException("Failed to launch a job, cause: ", e);
        }
    }
}
