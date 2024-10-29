package shop.S5G.shop.adapter;

import feign.HeaderMap;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.S5G.shop.dto.payment.TossPaymentConfirmRequestDto;

@FeignClient(url = "https://api.tosspayments.com/v1/payments")
public interface TossPaymentAdapter {
    // TODO: 결제 응답을 받아야함
    @PostMapping("/confirm")
    ResponseEntity<Void> confirm(@HeaderMap Map<String, Object> headers, @RequestBody TossPaymentConfirmRequestDto confirmRequest);
}
