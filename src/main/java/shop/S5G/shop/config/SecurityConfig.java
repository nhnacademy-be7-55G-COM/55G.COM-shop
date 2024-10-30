package shop.S5G.shop.config;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public String tossPaymentSecretKey() {
        return Base64.encodeBase64String(("This is Secret Key"+":").getBytes());
    }

    @Bean
    @Scope("prototype")
    public Map<String, Object> tossPaymentAuthHeader(@Qualifier("tossPaymentSecretKey") String secretKey) {
        Map<String, Object> header = new HashMap<>();
        header.put("Authorization", "Basic " + secretKey);
        header.put("Content-Type", "application/json");
        return header;
    }
}
