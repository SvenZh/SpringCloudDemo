package com.sven.batch.reader;

import java.util.List;

import org.springframework.batch.item.support.ListItemReader;

public class TestReader extends ListItemReader<String> {

    public TestReader(List<String> list) {
        super(list);
    }
}
