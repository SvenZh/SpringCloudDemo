package com.sven.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class TestProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(String item) throws Exception {
        Thread.sleep(200);
        System.out.println(Thread.currentThread().getName() + "-" + item);

        return item;
    }

}
