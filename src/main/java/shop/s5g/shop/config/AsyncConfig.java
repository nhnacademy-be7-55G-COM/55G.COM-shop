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
        executor.setMaxPoolSize(5);
        executor.setCorePoolSize(5);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("RabbitExe-");
        executor.initialize();

        return executor;
    }
}
