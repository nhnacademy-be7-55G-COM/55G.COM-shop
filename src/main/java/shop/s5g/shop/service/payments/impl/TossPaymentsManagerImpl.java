package shop.s5g.shop.service.payments.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.s5g.shop.adapter.TossPaymentsAdapter;
import shop.s5g.shop.dto.payments.TossPaymentsCancelRequestDto;
import shop.s5g.shop.dto.payments.TossPaymentsConfirmRequestDto;
import shop.s5g.shop.dto.payments.TossPaymentsDto;
import shop.s5g.shop.entity.Payment;
import shop.s5g.shop.entity.order.Order;
import shop.s5g.shop.repository.order.OrderRepository;
import shop.s5g.shop.repository.payments.TossPaymentRepository;
import shop.s5g.shop.service.payments.AbstractPaymentManager;

@RequiredArgsConstructor
@Service("tossPaymentsManager")
public class TossPaymentsManagerImpl extends AbstractPaymentManager {
    private final OrderRepository orderRepository;
    private final TossPaymentsAdapter adapter;
    private final TossPaymentRepository paymentRepository;

    @Override
    protected Object confirmPaymentAdapter(long orderDataId, Map<String, Object> request) {
        TossPaymentsConfirmRequestDto confirmDto = TossPaymentsConfirmRequestDto.of(request);

        ResponseEntity<TossPaymentsDto> response = adapter.confirm(confirmDto);
        // 근데 어차피 feignClient 는 20x, 30x가 아니면 알아서 예외를 반환해버림..
//        if (!response.getStatusCode().is2xxSuccessful()) {
//            throw new RuntimeException();
//        }

        // TODO: 적절한 예외로 바꾸기!
        Order order = orderRepository.findById(orderDataId).orElseThrow(
            () -> new RuntimeException()
        );
//
        paymentRepository.save(new Payment(order, confirmDto.paymentKey(), "KRW", confirmDto.amount(), confirmDto.orderId()));

        return response.getBody();
    }

    @Override
    protected Object cancelPaymentAdapter(String paymentKey, Map<String, Object> request) {
        ObjectMapper objectMapper = new ObjectMapper();
        TossPaymentsCancelRequestDto cancelRequest = objectMapper.convertValue(request, TossPaymentsCancelRequestDto.class);

        return adapter.cancel(paymentKey, cancelRequest).getBody();
    }
}
