package shop.s5g.shop.controller;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo.InstanceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.actuator.ApplicationStatus;

@RestController
@RequestMapping("/actuator/status")
@RequiredArgsConstructor
public class ApplicationStatusController {
    private final ApplicationInfoManager applicationInfoManager;
    private final ApplicationStatus applicationStatus;

    @PostMapping
    public void changeStatus() {
        applicationInfoManager.setInstanceStatus(InstanceStatus.DOWN);
        applicationStatus.stopService();
    }

}
