package com.example.processing.writer;

import com.example.processing.Organization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@StepScope
@Component
@Slf4j
public class OrganizationWriter extends JsonFileItemWriter<Organization> {

    public OrganizationWriter(@Value("#{stepExecutionContext['destinationFilePath']}") String destinationFilePath) {
        super(new FileSystemResource(destinationFilePath), new JacksonJsonObjectMarshaller<>());

    }

    @Override
    public String doWrite(Chunk<? extends Organization> organizations) {
        log.info("Writing line, thread: {}", Thread.currentThread().getName());
        return super.doWrite(organizations);
    }
}
