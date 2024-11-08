package shop.s5g.shop.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import shop.s5g.shop.config.FeignAuthorizationConfig;
import shop.s5g.shop.dto.payments.TossPaymentsCancelRequestDto;
import shop.s5g.shop.dto.payments.TossPaymentsConfirmRequestDto;
import shop.s5g.shop.dto.payments.TossPaymentsDto;

@FeignClient(name = "tossPayments", url = "https://api.tosspayments.com/v1/payments", configuration = FeignAuthorizationConfig.class)
public interface TossPaymentsAdapter {
    @PostMapping("/confirm")
    ResponseEntity<TossPaymentsDto> confirm(@RequestBody TossPaymentsConfirmRequestDto confirmRequest);

    @GetMapping("/{paymentKey}")
    ResponseEntity<TossPaymentsDto> queryPayByPaymentKey(@PathVariable String paymentKey);

    @GetMapping("/orders/{orderId}")
    ResponseEntity<TossPaymentsDto> queryPayByOrderId(@PathVariable String orderId);

    @PostMapping("/{paymentKey}/cancel")
    ResponseEntity<TossPaymentsDto> cancel(@PathVariable String paymentKey, @RequestBody
        TossPaymentsCancelRequestDto cancelRequest);
}
