package shop.s5g.shop.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {
    @Bean("rabbitExecutor")
    public Executor rabbitExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(10);
        executor.setCorePoolSize(10);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("RabbitExec-");
        executor.initialize();

        return executor;
    }
}
