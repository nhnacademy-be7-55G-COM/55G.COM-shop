package shop.S5G.shop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.S5G.shop.security.ShopMemberDetail;

@RestController
public class TestController {
    @GetMapping("/api/shop/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok().body("test");
    }

    @GetMapping("/api/shop/test/auth")
    public ResponseEntity<String> testAuth(@AuthenticationPrincipal ShopMemberDetail memberDetail) {
        return ResponseEntity.ok().body(
            String.format("your id: %s\nyour pk: %d", memberDetail.getLoginId(), memberDetail.getCustomerId())
        );
    }
}
