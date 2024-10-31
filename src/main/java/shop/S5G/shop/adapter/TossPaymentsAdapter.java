package shop.S5G.shop.adapter;

import feign.HeaderMap;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.S5G.shop.dto.payments.TossPaymentsConfirmRequestDto;
import shop.S5G.shop.dto.payments.TossPaymentsCancelRequestDto;
import shop.S5G.shop.dto.payments.TossPaymentsDto;

@FeignClient(name = "tossPayments", url = "https://api.tosspayments.com/v1/payments")
public interface TossPaymentsAdapter {
    @PostMapping("/confirm")
    ResponseEntity<TossPaymentsDto> confirm(@HeaderMap Map<String, Object> headers, @RequestBody TossPaymentsConfirmRequestDto confirmRequest);

    @GetMapping("/{paymentKey}")
    ResponseEntity<TossPaymentsDto> queryPayByPaymentKey(@HeaderMap Map<String, Object> headers, @PathVariable String paymentKey);

    @GetMapping("/orders/{orderId}")
    ResponseEntity<TossPaymentsDto> queryPayByOrderId(@HeaderMap Map<String, Object> headers, @PathVariable String orderId);

    @PostMapping("/{paymentKey}/cancel")
    ResponseEntity<TossPaymentsDto> cancel(@HeaderMap Map<String, Object> headers, @PathVariable String paymentKey, @RequestBody
        TossPaymentsCancelRequestDto cancelRequest);
}
