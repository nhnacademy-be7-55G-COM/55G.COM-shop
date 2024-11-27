package shop.s5g.shop.service.order.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.order.OrderDetailWithBookResponseDto;
import shop.s5g.shop.entity.order.OrderDetail;
import shop.s5g.shop.entity.order.OrderDetailType;
import shop.s5g.shop.exception.order.OrderDetailsNotExistException;
import shop.s5g.shop.repository.order.OrderDetailRepository;
import shop.s5g.shop.repository.order.OrderDetailTypeRepository;
import shop.s5g.shop.service.order.OrderDetailService;

@RequiredArgsConstructor
@Service
@Transactional
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailTypeRepository orderDetailTypeRepository;

    @Override
    @Transactional(readOnly = true)
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
    public void changeOrderDetailType(long detailId, String type) {
        OrderDetail detail = orderDetailRepository.findById(detailId).orElseThrow(
            () -> new OrderDetailsNotExistException("OrderDetail is not exist for id="+detailId)
        );

        OrderDetailType typeEntity = orderDetailTypeRepository.findStatusByName(type);

        detail.setOrderDetailType(typeEntity);
    }
}
