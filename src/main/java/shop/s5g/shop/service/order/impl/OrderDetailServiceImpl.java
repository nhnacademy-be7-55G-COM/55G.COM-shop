package shop.s5g.shop.service.order.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.s5g.shop.dto.order.OrderDetailWithBookResponseDto;
import shop.s5g.shop.entity.order.OrderDetail;
import shop.s5g.shop.entity.order.OrderDetailType;
import shop.s5g.shop.entity.order.OrderDetailType.Type;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.exception.order.OrderDetailsNotExistException;
import shop.s5g.shop.repository.order.OrderDetailRepository;
import shop.s5g.shop.repository.order.OrderDetailTypeRepository;
import shop.s5g.shop.service.delivery.DeliveryService;
import shop.s5g.shop.service.order.OrderDetailService;

@RequiredArgsConstructor
@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailTypeRepository orderDetailTypeRepository;
    private final DeliveryService deliveryService;

    @Override
    public List<OrderDetailWithBookResponseDto> getOrderDetailsWithBook(long orderId) {
        // join 이 많아서 일단 존재유무를 확인하고 날림.
        // TODO: 어떤것이 성능이 더 좋은지?
        if (orderDetailRepository.countOrderDetailsByOrderId(orderId) > 0) {
            return orderDetailRepository.queryAllDetailsByOrderId(orderId);
        } else {
            throw new OrderDetailsNotExistException(String.format("OrderDetails do not exist for [%d]", orderId));
        }
    }

    @Override
    public void cancelOrderDetail(long orderDetailId) {
        // TODO: 만약 모든 주문이 취소되었다면 배송도 완료 혹은 취소되어야 함.
        OrderDetail orderDetail =  orderDetailRepository.findById(orderDetailId).orElseThrow(
            () -> new OrderDetailsNotExistException("주문을 찾을 수 없습니다.")
        );

        if (!orderDetail.getOrderDetailType().getName().equals(Type.COMPLETE.name())) {
            throw new BadRequestException("주문 완료 상태만 취소가 가능합니다.");
        }

        OrderDetailType type = orderDetailTypeRepository.findStatusByName(Type.CANCEL);

        orderDetail.setOrderDetailType(type);
        deliveryService.deliveryCheckForCancel(orderDetail.getOrder().getId());
    }
}
