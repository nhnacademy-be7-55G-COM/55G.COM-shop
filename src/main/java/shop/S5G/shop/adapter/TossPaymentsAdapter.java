package shop.S5G.shop.adapter;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import shop.S5G.shop.dto.payments.TossPaymentsCancelRequestDto;
import shop.S5G.shop.dto.payments.TossPaymentsConfirmRequestDto;
import shop.S5G.shop.dto.payments.TossPaymentsDto;

@FeignClient(name = "tossPayments", url = "https://api.tosspayments.com/v1/payments")
public interface TossPaymentsAdapter {
    @PostMapping("/confirm")
    ResponseEntity<TossPaymentsDto> confirm(@RequestHeader Map<String, Object> headers, @RequestBody TossPaymentsConfirmRequestDto confirmRequest);

    @GetMapping("/{paymentKey}")
    ResponseEntity<TossPaymentsDto> queryPayByPaymentKey(@RequestHeader Map<String, Object> headers, @PathVariable String paymentKey);

    @GetMapping("/orders/{orderId}")
    ResponseEntity<TossPaymentsDto> queryPayByOrderId(@RequestHeader Map<String, Object> headers, @PathVariable String orderId);

    @PostMapping("/{paymentKey}/cancel")
    ResponseEntity<TossPaymentsDto> cancel(@RequestHeader Map<String, Object> headers, @PathVariable String paymentKey, @RequestBody
        TossPaymentsCancelRequestDto cancelRequest);
}
