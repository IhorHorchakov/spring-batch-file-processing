package com.example;

public interface ApplicationProperties {
    /* Data source properties */
    String DATA_SOURCE_DB_TYPE = "H2";
    String DATA_SOURCE_DB_NAME = "testDB;MODE=MySQL";

    /* Data processing properties */
    String DATA_PROCESSING_SOURCE_FILE_PATH = "files/source/organizations-300k-records.csv";
    String DATA_PROCESSING_DESTINATION_FOLDER_PATH = "files/destination/organizations";
    int DATA_PROCESSING_CHUNK_SIZE = 10_000;
    int TASK_EXECUTOR_THREAD_POOL_SIZE = 3;
    int DATA_PROCESSING_PARTITIONS_NUMBER = 3;
}
