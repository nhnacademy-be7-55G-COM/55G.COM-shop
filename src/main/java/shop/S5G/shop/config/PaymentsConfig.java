package shop.S5G.shop.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class PaymentsConfig {
    @Bean("tossPaymentsAuthHeader")
    @Scope("prototype")
    public Map<String, Object> tossPaymentsAuthHeader(@Qualifier("tossPaymentsSecretKey") String secretKey) {
        Map<String, Object> header = new HashMap<>();
        header.put("Authorization", "Basic " + secretKey);
        return header;
    }
}
