package shop.s5g.shop.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import shop.s5g.shop.dto.auth.UserDetailResponseDto;

@FeignClient(value = "auth-service")
public interface AuthAdapter {

    @GetMapping("/api/auth/id/{uuid}")
    ResponseEntity<UserDetailResponseDto> getUserDetails(@PathVariable("uuid") String uuid);
}
