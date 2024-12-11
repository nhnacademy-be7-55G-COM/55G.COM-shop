package shop.s5g.shop.service.payments.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import shop.s5g.shop.adapter.TossPaymentsAdapter;
import shop.s5g.shop.dto.payments.TossPaymentsCancelSimpleRequestDto;
import shop.s5g.shop.dto.payments.TossPaymentsDto;
import shop.s5g.shop.entity.order.Order;
import shop.s5g.shop.exception.ResourceNotFoundException;
import shop.s5g.shop.repository.order.OrderRepository;
import shop.s5g.shop.repository.payments.TossPaymentRepository;

// protected 메소드 테스트를 위해 같은 패키지로 두었음.
@ExtendWith(MockitoExtension.class)
class TossPaymentsManagerImplTest {
    @Mock
    OrderRepository orderRepository;
    @Mock
    TossPaymentsAdapter adapter;
    @Mock
    TossPaymentRepository tossPaymentRepository;

    TossPaymentsManagerImpl manager;

    final long orderDataId = 1L;
    Map<String, Object> request = new HashMap<>();
    TossPaymentsDto mockDto = mock(TossPaymentsDto.class);
    ResponseEntity<TossPaymentsDto> mockResponse = ResponseEntity.ok(mockDto);
    Order mockOrder = mock(Order.class);

    final String pk = "Ddf34hJ49v9";

    @BeforeEach
    void initRequest() {
        manager = spy(new TossPaymentsManagerImpl(orderRepository, adapter, tossPaymentRepository));

        request.put("paymentKey", pk);
        request.put("orderId", "9vVf3kud0D");
        request.put("amount", 5000L);
    }

    @Test
    void confirmPaymentAdapterTest() {
        when(adapter.confirm(any())).thenReturn(mockResponse);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(mockOrder));

        assertThat(manager.confirmPaymentAdapter(orderDataId, request))
            .isSameAs(mockResponse.getBody());

        verify(tossPaymentRepository, times(1)).save(any());
    }

    @Test
    void confirmPaymentAdapter_NotFoundOrder() {
        when(adapter.confirm(any())).thenReturn(mockResponse);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> manager.confirmPaymentAdapter(orderDataId, request))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(tossPaymentRepository, never()).save(any());
    }

    @Test
    void cancelPaymentAdapterTest() {
        Map<String, Object> cancelRequest = new HashMap<>();
        cancelRequest.put("cancelReason", "아무튼 환불해 주세요");
        cancelRequest.put("cancelAmount", 5000L);

        when(adapter.cancel(anyString(), any(TossPaymentsCancelSimpleRequestDto.class))).thenReturn(mockResponse);

        assertThat(manager.cancelPaymentAdapter(pk, cancelRequest))
            .isSameAs(mockDto);

        verify(adapter, times(1)).cancel(eq(pk), any(TossPaymentsCancelSimpleRequestDto.class));
    }
}
