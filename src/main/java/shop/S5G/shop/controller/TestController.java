package shop.S5G.shop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/api/shop/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok().body("test");
    }
}
