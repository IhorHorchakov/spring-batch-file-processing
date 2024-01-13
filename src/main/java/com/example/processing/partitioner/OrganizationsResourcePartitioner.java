package com.example.processing.partitioner;


import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


public class OrganizationsResourcePartitioner implements Partitioner {
    private final static String EXECUTION_CONTEXT_LINES_TO_SKIP_KEY = "linesToSkip";
    private final static String EXECUTION_CONTEXT_MAX_ITEM_COUNT_KEY = "maxItemCount";
    private final static String EXECUTION_CONTEXT_SOURCE_FILE_PATH_KEY = "sourceFilePath";
    private final static String EXECUTION_CONTEXT_DESTINATION_FILE_PATH_KEY = "destinationFilePath";
    private final static String PARTITION_KEYWORD = "partition";

    private String sourceFilePath;
    private String destinationFolderPath;

    public OrganizationsResourcePartitioner(String sourceFilePath, String destinationFolderPath) {
        this.sourceFilePath = sourceFilePath;
        this.destinationFolderPath = destinationFolderPath;
    }

    @Override
    public Map<String, ExecutionContext> partition(int numberOfPartitions) {
        int numberOfLines = countNumberOfLines();
        if (numberOfLines == 0 || numberOfPartitions == 0) {
            return Collections.emptyMap();
        }
        return buildPartitions(numberOfLines, numberOfPartitions);
    }

    private Map<String, ExecutionContext> buildPartitions(int numberOfLines, int numberOfPartitions) {
        Map<String, ExecutionContext> partitions = new HashMap<>();
        int partitionSize = numberOfLines / numberOfPartitions;
        for (int lineIndex = 1, partitionIndex = 0; partitionIndex < numberOfPartitions; partitionIndex++) {
            putPartition(partitions, partitionIndex, lineIndex, partitionSize);
            lineIndex += partitionSize;
        }
        return partitions;
    }

    private void putPartition(Map<String, ExecutionContext> partitions, int partitionIndex, int linesToSkip, int partitionSize) {
        ExecutionContext executionContext = new ExecutionContext();
        executionContext.putInt(EXECUTION_CONTEXT_LINES_TO_SKIP_KEY, linesToSkip);
        executionContext.putInt(EXECUTION_CONTEXT_MAX_ITEM_COUNT_KEY, partitionSize);
        executionContext.putString(EXECUTION_CONTEXT_SOURCE_FILE_PATH_KEY, sourceFilePath);
        executionContext.putString(EXECUTION_CONTEXT_DESTINATION_FILE_PATH_KEY, destinationFolderPath + "/organizationPartition" + partitionIndex + ".json");
        partitions.put(PARTITION_KEYWORD + partitionIndex, executionContext);
    }

    private int countNumberOfLines() {
        try (Stream<String> fileStream = Files.lines(Paths.get(sourceFilePath))) {
            return (int) fileStream.count();
        } catch (IOException e) {
            throw new RuntimeException("Failed to count the number of lines", e);
        }
    }
}
