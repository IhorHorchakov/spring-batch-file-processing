package com.example.processing.reader;

import com.example.processing.Organization;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.PathResource;
import org.springframework.stereotype.Component;


@StepScope
@Component
public class OrganizationReader extends FlatFileItemReader<Organization> {

    public OrganizationReader(
            @Value("#{stepExecutionContext['sourceFilePath']}") String sourceFilePath,
            @Value("#{stepExecutionContext['linesToSkip']}") int linesToSkip,
            @Value("#{stepExecutionContext['maxItemCount']}") int maxItemCount) {
        this.setResource(new PathResource(sourceFilePath));
        this.setLineMapper(new OrganizationMapper());
        this.setLinesToSkip(linesToSkip);
        this.setMaxItemCount(maxItemCount);
    }

    @Override
    public Organization read() throws Exception {
        return super.read();
    }
}
