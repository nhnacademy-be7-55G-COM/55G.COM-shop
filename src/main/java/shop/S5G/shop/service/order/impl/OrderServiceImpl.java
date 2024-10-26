package shop.S5G.shop.service.order.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.S5G.shop.dto.order.OrderWithDetailResponseDto;
import shop.S5G.shop.entity.order.Order;
import shop.S5G.shop.repository.order.OrderRepository;
import shop.S5G.shop.service.order.OrderService;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Override
    public Page<Order> findAllByCustomerId(long customerId, Pageable pageable) {
        return orderRepository.findAllByCustomerCustomerId(customerId, pageable);
    }

    @Override
    public Page<OrderWithDetailResponseDto> queryAllOrdersByCustomerId(long customerId,
        Pageable pageable) {
        return orderRepository.findOrdersByCustomerId(customerId, pageable);
    }
}
