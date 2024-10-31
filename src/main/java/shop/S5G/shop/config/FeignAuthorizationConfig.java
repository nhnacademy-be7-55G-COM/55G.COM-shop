package shop.S5G.shop.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import shop.S5G.shop.interceptor.FeignTossPaymentsInterceptor;

public class FeignAuthorizationConfig {

    @Bean
    public RequestInterceptor requestInterceptor(@Qualifier("tossPaymentsSecretKey") String secretKey) {
        return new FeignTossPaymentsInterceptor(secretKey);
    }
}
