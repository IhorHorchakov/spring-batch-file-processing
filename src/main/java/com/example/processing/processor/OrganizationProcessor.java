package com.example.processing.processor;

import com.example.processing.Organization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@StepScope
@Component
public class OrganizationProcessor implements ItemProcessor<Organization, Organization> {
    @Override
    public Organization process(Organization organization) {
        log.info("Processing in thread name: {}", Thread.currentThread().getName());

        // emulating a business logic process
        return convert(organization);
    }

    private Organization convert(Organization source) {
        Organization target = new Organization();
        target.setIndex(source.getIndex());
        target.setOrganizationId(source.getOrganizationId());
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setCountry(source.getCountry());
        target.setIndustry(source.getIndustry());
        target.setWebsite(source.getWebsite());
        target.setFounded(source.getFounded());
        target.setNumberOfEmployees(source.getNumberOfEmployees());
        return target;
    }
}
