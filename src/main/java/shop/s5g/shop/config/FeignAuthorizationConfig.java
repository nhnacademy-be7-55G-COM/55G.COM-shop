package shop.s5g.shop.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import shop.s5g.shop.interceptor.FeignTossPaymentsInterceptor;

public class FeignAuthorizationConfig {

    @Bean
    public RequestInterceptor requestInterceptor(@Qualifier("tossPaymentsSecretKey") String secretKey) {
        return new FeignTossPaymentsInterceptor(secretKey);
    }
}
