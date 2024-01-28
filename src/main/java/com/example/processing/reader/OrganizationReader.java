package com.example.processing.reader;

import com.example.processing.Organization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.PathResource;


@Slf4j
public class OrganizationReader extends FlatFileItemReader<Organization> {

    public OrganizationReader(String sourceFilePath, int linesToSkip, int maxItemCount) {
        this.setResource(new PathResource(sourceFilePath));
        this.setLineMapper(new OrganizationMapper());
        this.setLinesToSkip(linesToSkip);
        this.setMaxItemCount(maxItemCount);
    }

    @Override
    public Organization read() throws Exception {
        log.info("Reading line, thread: {}", Thread.currentThread().getName());
        return super.read();
    }
}
