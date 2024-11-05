package shop.S5G.shop.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomHealthIndicator implements HealthIndicator {
    private final ApplicationStatus applicationStatus;

    @Override
    public Health health() {
        if (!applicationStatus.getStatus()) {
            return Health.down().build();
        }
        return Health.up().build();
    }
}
