package com.example.processing;

import lombok.Data;

@Data
public class Organization {
    private String index;
    private String organizationId;
    private String name;
    private String website;
    private String country;
    private String description;
    private int founded;
    private String industry;
    private int numberOfEmployees;
}
