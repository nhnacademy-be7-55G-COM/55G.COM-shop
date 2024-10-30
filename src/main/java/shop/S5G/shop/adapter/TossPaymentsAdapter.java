package shop.S5G.shop.adapter;

import feign.HeaderMap;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.S5G.shop.dto.payment.TossPaymentConfirmRequestDto;
import shop.S5G.shop.dto.payment.TossPaymentsCancelRequestDto;
import shop.S5G.shop.dto.payment.TossPaymentsDto;

@FeignClient(name = "tossPayments", url = "https://api.tosspayments.com/v1/payments")
public interface TossPaymentsAdapter {
    @PostMapping("/confirm")
    ResponseEntity<TossPaymentsDto> confirm(@HeaderMap Map<String, Object> headers, @RequestBody TossPaymentConfirmRequestDto confirmRequest);

    @GetMapping("/{paymentKey}")
    ResponseEntity<TossPaymentsDto> queryPayByPaymentKey(@HeaderMap Map<String, Object> headers, @PathVariable String paymentKey);

    @GetMapping("/orders/{orderId}")
    ResponseEntity<TossPaymentsDto> queryPayByOrderId(@HeaderMap Map<String, Object> headers, @PathVariable String orderId);

    @PostMapping("/{paymentKey}/cancel")
    ResponseEntity<TossPaymentsDto> cancelPay(@HeaderMap Map<String, Object> headers, @PathVariable String paymentKey, @RequestBody
        TossPaymentsCancelRequestDto cancelRequest);
}
