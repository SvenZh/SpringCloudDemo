package com.sven.batch.conf;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import com.sven.batch.processor.TestProcessor;
import com.sven.batch.reader.TestReader;
import com.sven.batch.write.TestWrite;

@EnableBatchProcessing
@Configuration
public class BatchServiceConfig {
    @Autowired
    private TestProcessor testProcessor;

    @Autowired
    private TestWrite testWrite;

    // @Autowired
    // private TestReader testReader;

    @Bean
    public TestReader testReader() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            data.add("data" + i);
        }
        return new TestReader(data);
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
            TaskExecutor batchTaskExecutor) {
        return new StepBuilder("step1", jobRepository)
                .<String, String>chunk(100, transactionManager)
                .reader(testReader())
                .processor(testProcessor)
                .writer(testWrite)
                .taskExecutor(batchTaskExecutor)
                .build();
    }

    @Bean
    public Job job(JobRepository jobRepository, Step step1) {
        return new JobBuilder("job", jobRepository)
                .start(step1)
                .build();
    }

    @Bean
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // 核心线程数
        executor.setMaxPoolSize(10); // 最大线程数
        executor.setQueueCapacity(25); // 队列容量
        executor.setThreadNamePrefix("batch-"); // 线程名前缀
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

}
