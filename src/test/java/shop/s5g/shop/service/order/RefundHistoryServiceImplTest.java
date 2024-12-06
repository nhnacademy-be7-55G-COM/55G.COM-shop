package shop.s5g.shop.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.shop.dto.refund.OrderDetailDeliveryPairContainer;
import shop.s5g.shop.dto.refund.RefundHistoryCreateRequestDto;
import shop.s5g.shop.dto.refund.RefundHistoryResponseDto;
import shop.s5g.shop.entity.delivery.Delivery;
import shop.s5g.shop.entity.delivery.DeliveryFee;
import shop.s5g.shop.entity.order.OrderDetail;
import shop.s5g.shop.entity.order.OrderDetailType;
import shop.s5g.shop.entity.refund.RefundHistory;
import shop.s5g.shop.entity.refund.RefundType;
import shop.s5g.shop.entity.refund.RefundType.Type;
import shop.s5g.shop.exception.EssentialDataNotFoundException;
import shop.s5g.shop.exception.refund.RefundAlreadyProceedException;
import shop.s5g.shop.exception.refund.RefundConditionNotFulfilledException;
import shop.s5g.shop.repository.order.OrderDetailRepository;
import shop.s5g.shop.repository.order.OrderDetailTypeRepository;
import shop.s5g.shop.repository.order.RefundHistoryRepository;
import shop.s5g.shop.repository.order.RefundImageRepository;
import shop.s5g.shop.repository.order.RefundTypeRepository;
import shop.s5g.shop.service.order.impl.RefundHistoryServiceImpl;
import shop.s5g.shop.service.point.PointHistoryService;

@ExtendWith(MockitoExtension.class)
class RefundHistoryServiceImplTest {
    @Mock
    RefundHistoryRepository refundHistoryRepository;
    @Mock
    RefundImageRepository refundImageRepository;
    @Mock
    RefundTypeRepository refundTypeRepository;
    @Mock
    OrderDetailRepository orderDetailRepository;
    @Mock
    PointHistoryService pointHistoryService;
    @Mock
    OrderDetailTypeRepository orderDetailTypeRepository;

    @InjectMocks
    RefundHistoryServiceImpl service;

    @Test
    void getRefundHistoryTest() {
        RefundHistoryResponseDto response = mock(RefundHistoryResponseDto.class);
        when(refundHistoryRepository.fetchRefundHistoryByOrderDetailId(anyLong())).thenReturn(response);

        assertThatCode(() -> service.getRefundHistory(1L))
            .doesNotThrowAnyException();

        verify(refundHistoryRepository, times(1)).fetchRefundHistoryByOrderDetailId(1L);
    }
    OrderDetail mockDetail = mock(OrderDetail.class);
    Delivery mockDelivery = mock(Delivery.class);

    OrderDetailType normalType = new OrderDetailType(1, "COMPLETE");
    OrderDetailType confirmType = new OrderDetailType(2, "CONFIRM");
    OrderDetailType refundType = new OrderDetailType(3, "REFUND");

    RefundType normalRefundType = new RefundType(1, "테스트환불타입", true);
    RefundType damagedRefundType = new RefundType(Type.DAMAGED.getTypeId(), Type.DAMAGED.getTypeName(), true);

    DeliveryFee fee = new DeliveryFee(100L, 0L, 100, "테스트 비용");

    OrderDetailDeliveryPairContainer pairContainer = new OrderDetailDeliveryPairContainer(
        mockDetail, mockDelivery
    );

    RefundHistoryCreateRequestDto create = new RefundHistoryCreateRequestDto(
        1L, 1L, "아무튼환불좀", List.of("test.png")
    );

    @Test
    void createNewRefundFail_OrderDetailTypeDoesNotExist() {
        when(mockDetail.getOrderDetailType()).thenReturn(normalType);

        when(orderDetailRepository.fetchOrderDetailAndDelivery(anyLong())).thenReturn(pairContainer);
        // basicRefundPoint
        when(refundTypeRepository.findTypeById(anyLong())).thenReturn(normalRefundType);

        when(mockDetail.getTotalPrice()).thenReturn(1000L);
        when(mockDetail.getAccumulationPrice()).thenReturn(10);
        when(mockDelivery.getDeliveryFee()).thenReturn(fee);
        when(mockDelivery.getShippingDate()).thenReturn(LocalDate.now());

        // 예외조건
        when(orderDetailTypeRepository.findStatusByName(any(OrderDetailType.Type.class))).thenThrow(
            EssentialDataNotFoundException.class
        );

        assertThatThrownBy(() -> service.createNewRefund(1L, create))
            .isInstanceOf(EssentialDataNotFoundException.class);

        // 어차피 rollback 되긴 하겠지만 저장되었는지 아닌지만 체크.
        verify(refundHistoryRepository, never()).save(any());
    }

    @Test
    void createNewRefundFail_RefundAlreadyProceedException() {
        when(orderDetailTypeRepository.findStatusByName(any(OrderDetailType.Type.class))).thenReturn(refundType);
        when(orderDetailRepository.fetchOrderDetailAndDelivery(anyLong())).thenReturn(pairContainer);
        // basicRefundPoint
        when(refundTypeRepository.findTypeById(anyLong())).thenReturn(normalRefundType);

        when(mockDetail.getTotalPrice()).thenReturn(1000L);
        when(mockDetail.getAccumulationPrice()).thenReturn(10);
        when(mockDelivery.getDeliveryFee()).thenReturn(fee);
        when(mockDelivery.getShippingDate()).thenReturn(LocalDate.now());

        // 예외조건
        when(mockDetail.getOrderDetailType()).thenReturn(refundType);

        assertThatThrownBy(() -> service.createNewRefund(1L, create))
            .isInstanceOf(RefundAlreadyProceedException.class);

        // 어차피 rollback 되긴 하겠지만 저장되었는지 아닌지만 체크.
        verify(refundHistoryRepository, never()).save(any());
    }

    @Test
    void createNewRefundFail_baseRefundPoint_ConditionNotFulfilled() {
        when(mockDetail.getOrderDetailType()).thenReturn(normalType);

        when(orderDetailRepository.fetchOrderDetailAndDelivery(anyLong())).thenReturn(pairContainer);
        // basicRefundPoint
        when(refundTypeRepository.findTypeById(anyLong())).thenReturn(normalRefundType);

        when(mockDetail.getTotalPrice()).thenReturn(1000L);
        when(mockDetail.getAccumulationPrice()).thenReturn(10);
        when(mockDelivery.getDeliveryFee()).thenReturn(fee);

        // 예외조건(20일 이전 주문)
        when(mockDelivery.getShippingDate()).thenReturn(LocalDate.now().minusDays(20L));

        assertThatThrownBy(() -> service.createNewRefund(1L, create))
            .isInstanceOf(RefundConditionNotFulfilledException.class)
            .hasMessageContaining("일반");

        // basicRefundPoint가 끝나기 전에 예외가 던져짐.
        verify(refundHistoryRepository, never()).save(any());
        verify(pointHistoryService, never()).createPointHistory(anyLong(), any());
    }

    @Test
    void createNewRefundFail_damagedRefundPoint_ConditionNotFulfilled() {
        when(mockDetail.getOrderDetailType()).thenReturn(normalType);

        when(orderDetailRepository.fetchOrderDetailAndDelivery(anyLong())).thenReturn(pairContainer);
        // damagedRefundPoint
        when(refundTypeRepository.findTypeById(anyLong())).thenReturn(damagedRefundType);

        when(mockDetail.getTotalPrice()).thenReturn(1000L);
        when(mockDetail.getAccumulationPrice()).thenReturn(10);
        when(mockDelivery.getDeliveryFee()).thenReturn(fee);

        // 예외조건(40일 이전 주문)
        when(mockDelivery.getShippingDate()).thenReturn(LocalDate.now().minusDays(40L));

        assertThatThrownBy(() -> service.createNewRefund(1L, create))
            .isInstanceOf(RefundConditionNotFulfilledException.class)
            .hasMessageContaining("파손/파본");

        // basicRefundPoint가 끝나기 전에 예외가 던져짐.
        verify(refundHistoryRepository, never()).save(any());
        verify(pointHistoryService, never()).createPointHistory(anyLong(), any());
    }

    @Test
    void createNewRefundFail_AlreadyConfirmedOrderDetail() {
        when(orderDetailRepository.fetchOrderDetailAndDelivery(anyLong())).thenReturn(pairContainer);
        // 예외조건
        when(mockDetail.getOrderDetailType()).thenReturn(confirmType);


        assertThatThrownBy(() -> service.createNewRefund(1L, create))
            .isInstanceOf(RefundConditionNotFulfilledException.class)
            .hasMessageContaining("확정");

        // basicRefundPoint가 끝나기 전에 예외가 던져짐.
        verify(refundHistoryRepository, never()).save(any());
        verify(pointHistoryService, never()).createPointHistory(anyLong(), any());
    }

    @Test
    void createNewRefundFail_RefundTypeNotFound() {
        when(orderDetailRepository.fetchOrderDetailAndDelivery(anyLong())).thenReturn(pairContainer);
        // 예외조건
        when(refundTypeRepository.findTypeById(anyLong())).thenThrow(EssentialDataNotFoundException.class);

        assertThatThrownBy(() -> service.createNewRefund(1L, create))
            .isInstanceOf(EssentialDataNotFoundException.class);

        // basicRefundPoint가 끝나기 전에 예외가 던져짐.
        verify(refundHistoryRepository, never()).save(any());
        verify(pointHistoryService, never()).createPointHistory(anyLong(), any());
    }

    RefundHistory mockHistory = mock(RefundHistory.class);

    @Test
    void createNewRefundSuccess() {
        when(mockDetail.getOrderDetailType()).thenReturn(normalType);

        when(orderDetailRepository.fetchOrderDetailAndDelivery(anyLong())).thenReturn(pairContainer);
        // basicRefundPoint
        when(refundTypeRepository.findTypeById(anyLong())).thenReturn(normalRefundType);

        when(mockDetail.getTotalPrice()).thenReturn(1000L);
        when(mockDetail.getAccumulationPrice()).thenReturn(10);
        when(mockDelivery.getDeliveryFee()).thenReturn(fee);
        when(mockDelivery.getShippingDate()).thenReturn(LocalDate.now());
        when(orderDetailTypeRepository.findStatusByName(any(OrderDetailType.Type.class))).thenReturn(refundType);

        when(refundHistoryRepository.save(any())).thenReturn(mockHistory);
        when(mockHistory.getId()).thenReturn(1L);

        assertThat(service.createNewRefund(1L, create))
            .hasFieldOrPropertyWithValue("refundId", 1L);

        // 어차피 rollback 되긴 하겠지만 저장되었는지 아닌지만 체크.
        verify(refundHistoryRepository, times(1)).save(any());
        verify(pointHistoryService, times(1)).createPointHistory(eq(1L), any());
        verify(refundImageRepository, times(1)).saveAll(anyList());
    }
}
