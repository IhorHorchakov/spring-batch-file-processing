package com.example.processing.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.File;

@Slf4j
public class FileDeletingTasklet implements Tasklet, InitializingBean {
    private Resource generatedFilesRoot;

    public FileDeletingTasklet(String generatedFilesRootPath) {
        this.generatedFilesRoot = new FileSystemResource(generatedFilesRootPath);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Executing tasklet, thread: {}", Thread.currentThread().getName());

        File destinationFolderFile = generatedFilesRoot.getFile();
        Assert.state(destinationFolderFile.isDirectory(), "Destination root is not a directory");

        File[] files = destinationFolderFile.listFiles();
        if (files != null) {
            for (File file : files) {
                boolean deleted = file.delete();
                if (!deleted) {
                    throw new UnexpectedJobExecutionException("Could not delete file " + file.getPath());
                }
            }
        }
        return RepeatStatus.FINISHED;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
