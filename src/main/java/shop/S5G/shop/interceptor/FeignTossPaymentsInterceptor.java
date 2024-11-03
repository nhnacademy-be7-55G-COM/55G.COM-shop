package shop.S5G.shop.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Qualifier;

public class FeignTossPaymentsInterceptor implements RequestInterceptor {
    private final String secretKey;

    public FeignTossPaymentsInterceptor(@Qualifier("tossPaymentsSecretKey") String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("Authorization", secretKey);
        requestTemplate.header("Content-Type", "application/json");
    }
}
