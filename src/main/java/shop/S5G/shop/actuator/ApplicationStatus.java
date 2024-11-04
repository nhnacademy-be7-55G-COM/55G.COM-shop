package shop.S5G.shop.actuator;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStatus {
    private boolean status = true;

    public void stopService() {
        this.status = false;
    }

    public boolean getStatus() {
        return status;
    }
}
