package org.zreddit59.service;

public interface CSVService {
    void appendLine( String topic, int subscribers, String messages );

    void close();
}
