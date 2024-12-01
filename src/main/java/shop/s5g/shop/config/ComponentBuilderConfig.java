package shop.s5g.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
public class ComponentBuilderConfig {

    public static final String IMAGE_URL_PATH = "https://image.toast.com/aaaacko/55gshop";
    public static final String IMAGE_LOCATION_PATH = "/55gshop/";

    @Bean("imageLocationBuilder")
    @Scope("prototype")
    public UriComponentsBuilder imageLocationBuilder() {
        return UriComponentsBuilder.fromHttpUrl(IMAGE_URL_PATH);
    }
}
