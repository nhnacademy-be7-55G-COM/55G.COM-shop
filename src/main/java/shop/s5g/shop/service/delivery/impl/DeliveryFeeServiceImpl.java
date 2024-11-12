package shop.s5g.shop.service.delivery.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.delivery.DeliveryFeeCreateRequestDto;
import shop.s5g.shop.dto.delivery.DeliveryFeeResponseDto;
import shop.s5g.shop.dto.delivery.DeliveryFeeUpdateRequestDto;
import shop.s5g.shop.entity.delivery.DeliveryFee;
import shop.s5g.shop.exception.ResourceNotFoundException;
import shop.s5g.shop.repository.delivery.DeliveryFeeRepository;
import java.util.List;
import shop.s5g.shop.service.delivery.DeliveryFeeService;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryFeeServiceImpl implements DeliveryFeeService {
    private final DeliveryFeeRepository deliveryFeeRepository;

    @Transactional(readOnly = true)
    @Override
    public List<DeliveryFeeResponseDto> getAllDeliveryFees() {
        return deliveryFeeRepository.findAll().stream().map(DeliveryFeeResponseDto::of).toList();
    }

    @Override
    public DeliveryFeeResponseDto createFee(DeliveryFeeCreateRequestDto createRequest) {
        DeliveryFee newFee = new DeliveryFee(createRequest);

        DeliveryFee saved = deliveryFeeRepository.save(newFee);
        return DeliveryFeeResponseDto.of(saved);
    }

    @Override
    public DeliveryFeeResponseDto updateFee(DeliveryFeeUpdateRequestDto updateRequest) {
        return deliveryFeeRepository.updateFee(updateRequest).map(DeliveryFeeResponseDto::of)
            .orElseThrow(
                () -> new ResourceNotFoundException(
                    "DeliveryFee cannot found: " + updateRequest.id())
            );
    }

    // delete는 현재 ERD에서 제공할 수 없음.
}
