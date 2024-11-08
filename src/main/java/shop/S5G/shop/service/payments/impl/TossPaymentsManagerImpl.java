package shop.S5G.shop.service.payments.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.S5G.shop.adapter.TossPaymentsAdapter;
import shop.S5G.shop.dto.payments.TossPaymentsCancelRequestDto;
import shop.S5G.shop.dto.payments.TossPaymentsConfirmRequestDto;
import shop.S5G.shop.dto.payments.TossPaymentsDto;
import shop.S5G.shop.repository.order.OrderRepository;
import shop.S5G.shop.repository.payments.TossPaymentRepository;
import shop.S5G.shop.service.payments.AbstractPaymentManager;

@RequiredArgsConstructor
@Service("tossPaymentsManager")
public class TossPaymentsManagerImpl extends AbstractPaymentManager {
    private final OrderRepository orderRepository;
    private final TossPaymentsAdapter adapter;
    private final TossPaymentRepository paymentRepository;

    @Override
    protected Object confirmPaymentAdapter(Map<String, Object> request) {
        TossPaymentsConfirmRequestDto confirmDto = TossPaymentsConfirmRequestDto.of(request);

        ResponseEntity<TossPaymentsDto> response = adapter.confirm(confirmDto);
        // 근데 어차피 feignClient 는 20x, 30x가 아니면 알아서 예외를 반환해버림..
//        if (!response.getStatusCode().is2xxSuccessful()) {
//            throw new RuntimeException();
//        }

        // TODO: 적절한 예외로 바꾸기!
//        Order order = orderRepository.findById(orderDataId).orElseThrow(
//            () -> new RuntimeException()
//        );
//
//        paymentRepository.save(new Payment(order, confirmRequest.paymentKey(), "KRW", confirmRequest.amount(), confirmRequest.orderId()));

        return response.getBody();
    }

    @Override
    protected Object cancelPaymentAdapter(String paymentKey, Map<String, Object> request) {
        ObjectMapper objectMapper = new ObjectMapper();
        TossPaymentsCancelRequestDto cancelRequest = objectMapper.convertValue(request, TossPaymentsCancelRequestDto.class);

        return adapter.cancel(paymentKey, cancelRequest).getBody();
    }
}
