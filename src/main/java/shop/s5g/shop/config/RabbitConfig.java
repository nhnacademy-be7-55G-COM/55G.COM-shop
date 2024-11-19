package shop.s5g.shop.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean("jacksonMessageConverter")
    public MessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
