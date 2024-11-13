package shop.s5g.shop.service.delivery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.shop.dto.delivery.DeliveryFeeCreateRequestDto;
import shop.s5g.shop.dto.delivery.DeliveryFeeUpdateRequestDto;
import shop.s5g.shop.entity.delivery.DeliveryFee;
import shop.s5g.shop.exception.ResourceNotFoundException;
import shop.s5g.shop.repository.delivery.DeliveryFeeRepository;
import shop.s5g.shop.service.delivery.impl.DeliveryFeeServiceImpl;

@ExtendWith(MockitoExtension.class)
class DeliveryFeeServiceImplTest {
    @Mock
    DeliveryFeeRepository deliveryFeeRepository;

    @InjectMocks
    DeliveryFeeServiceImpl service;

    @Test
    void getAllDeliveryFeesTest() {
        when(deliveryFeeRepository.findAll()).thenReturn(List.of());
        assertThatCode(() -> service.getAllDeliveryFees())
            .doesNotThrowAnyException();
        verify(deliveryFeeRepository, times(1)).findAll();
    }
    DeliveryFeeCreateRequestDto createRequest = new DeliveryFeeCreateRequestDto(
        5000L, 0L, 5000, "테스트"
    );

    @Test
    void createFeeTest() {
        DeliveryFee deliveryFee = new DeliveryFee(createRequest);
        when(deliveryFeeRepository.save(any())).thenReturn(deliveryFee);

        assertThat(service.createFee(createRequest))
            .hasFieldOrPropertyWithValue("fee", 5000L)
            .hasFieldOrPropertyWithValue("name", "테스트");

        verify(deliveryFeeRepository, times(1)).save(any());
    }

    DeliveryFeeUpdateRequestDto updateRequest = new DeliveryFeeUpdateRequestDto(
        1L, 2000L, 0L, 2000, "테스트 아님"
    );

    @Test
    void updateFeeFailTest() {
        when(deliveryFeeRepository.updateFee(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateFee(updateRequest))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(deliveryFeeRepository, times(1)).updateFee(updateRequest);
    }

    @Test
    void updateFeeSuccessTest() {
        DeliveryFee fee = new DeliveryFee(5000L, 0L, 5000, "테스트");
        when(deliveryFeeRepository.updateFee(any())).thenReturn(Optional.of(fee));

        assertThat(service.updateFee(updateRequest))
            .hasFieldOrPropertyWithValue("fee", 5000L)
            .hasFieldOrPropertyWithValue("name", "테스트");

        verify(deliveryFeeRepository, times(1)).updateFee(updateRequest);
    }

}
