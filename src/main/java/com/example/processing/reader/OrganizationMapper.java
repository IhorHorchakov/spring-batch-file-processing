package com.example.processing.reader;

import com.example.processing.Organization;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

public class OrganizationMapper extends DefaultLineMapper<Organization> {

    public OrganizationMapper() {
        this.setLineTokenizer(new DelimitedLineTokenizer() {
            {
                setNames("Index", "Organization Id", "Name", "Website", "Country", "Description", "Founded", "Industry", "Number of employees");
            }
        });
        this.setFieldSetMapper(new BeanWrapperFieldSetMapper<>() {
            {
                setTargetType(Organization.class);
            }
        });
    }
}
