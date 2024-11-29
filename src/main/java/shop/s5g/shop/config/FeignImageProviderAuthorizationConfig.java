package shop.s5g.shop.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class FeignImageProviderAuthorizationConfig {

    @Value("${image.secretKey}")
    private String secretKey;

    @Bean
    public RequestInterceptor secretKeyInjector() {
        return requestTemplate -> requestTemplate.header("Authorization", secretKey);
    }
}