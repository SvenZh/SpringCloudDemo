package com.sven.batch.write;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class TestWrite implements ItemWriter<String> {

    @Override
    public void write(Chunk<? extends String> items) throws Exception {
        for (String item : items) {
            System.out.println(item);
        }
    }

}
