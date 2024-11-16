package shop.s5g.shop.service.payments.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.s5g.shop.adapter.TossPaymentsAdapter;
import shop.s5g.shop.dto.payments.TossPaymentsCancelRequestDto;
import shop.s5g.shop.dto.payments.TossPaymentsCancelSimpleRequestDto;
import shop.s5g.shop.dto.payments.TossPaymentsConfirmRequestDto;
import shop.s5g.shop.dto.payments.TossPaymentsDto;
import shop.s5g.shop.entity.Payment;
import shop.s5g.shop.entity.order.Order;
import shop.s5g.shop.exception.ResourceNotFoundException;
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

        // OrderDetail의 존재로 해당 order는 항상 존재할 것으로 인지됨...
        Order order = orderRepository.findById(orderDataId).orElseThrow(
            () -> new ResourceNotFoundException("Order not found for given id: "+orderDataId)
        );
//
        paymentRepository.save(new Payment(order, confirmDto.paymentKey(), "KRW", confirmDto.amount(), confirmDto.orderId()));

        return response.getBody();
    }

    @Override
    protected Object cancelPaymentAdapter(String paymentKey, Map<String, Object> request) {
        ObjectMapper objectMapper = new ObjectMapper();

        // TODO: 이 if문에 대해 조금 더 생각을 해봐야함.
        if (request.containsKey("refundReceiveAccount")) {
            TossPaymentsCancelRequestDto cancelRequest = objectMapper.convertValue(request,
                TossPaymentsCancelRequestDto.class);

            return adapter.cancel(paymentKey, cancelRequest).getBody();
        } else {    // 현재 여기만 사용중.
            TossPaymentsCancelSimpleRequestDto cancelRequest = objectMapper.convertValue(request,
                TossPaymentsCancelSimpleRequestDto.class);

            return adapter.cancel(paymentKey, cancelRequest).getBody();
        }
    }
}
