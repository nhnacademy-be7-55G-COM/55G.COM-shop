package shop.s5g.shop.service.delivery;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.shop.dto.delivery.DeliveryUpdateRequestDto;
import shop.s5g.shop.entity.delivery.Delivery;
import shop.s5g.shop.entity.delivery.DeliveryStatus;
import shop.s5g.shop.exception.ResourceNotFoundException;
import shop.s5g.shop.repository.delivery.DeliveryRepository;
import shop.s5g.shop.repository.delivery.DeliveryStatusRepository;
import shop.s5g.shop.service.delivery.impl.DeliveryServiceImpl;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceImplTest {
    @Mock
    DeliveryRepository deliveryRepository;
    @Mock
    DeliveryStatusRepository deliveryStatusRepository;

    @InjectMocks
    DeliveryServiceImpl service;

    @Test
    void getDeliveryFailTest() {
        when(deliveryRepository.findByIdFetch(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getDelivery(1L))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(deliveryRepository, times(1)).findByIdFetch(1L);
    }


    DeliveryUpdateRequestDto update = mock(DeliveryUpdateRequestDto.class);
    Delivery delivery = mock(Delivery.class);
    DeliveryStatus deliveryStatus = new DeliveryStatus(1, "PREPARING");

    @Test
    void getDeliverySuccessTest() {
        when(delivery.getStatus()).thenReturn(deliveryStatus);
        when(deliveryRepository.findByIdFetch(anyLong())).thenReturn(Optional.of(delivery));

        assertThatCode(() -> service.getDelivery(1L))
            .doesNotThrowAnyException();

        verify(deliveryRepository, times(1)).findByIdFetch(1L);
    }
    @Test
    void adminUpdateDeliveryFailTest() {
        when(update.id()).thenReturn(1L);
        when(deliveryRepository.findByIdFetch(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.adminUpdateDelivery(update))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(deliveryRepository, times(1)).findByIdFetch(1L);
        verify(deliveryStatusRepository, never()).findByName(anyString());
        verify(delivery, never()).setStatus(any());
        verify(delivery, never()).setInvoiceNumber(any());
    }

    @Test
    void adminUpdateDeliveryStatusFailTest() {
        when(delivery.getStatus()).thenReturn(deliveryStatus);
        when(update.status()).thenReturn("zxcv");   // 이전과 다른 배송상태!
        when(deliveryRepository.findByIdFetch(anyLong())).thenReturn(Optional.of(delivery));
        when(deliveryStatusRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.adminUpdateDelivery(update))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(deliveryRepository, times(1)).findByIdFetch(anyLong());
        verify(deliveryStatusRepository, times(1)).findByName(anyString());
        verify(delivery, never()).setStatus(any());
        verify(delivery, never()).setInvoiceNumber(any());
    }

    DeliveryUpdateRequestDto updateRequest = new DeliveryUpdateRequestDto(
        1L, "Address", "1234",
        LocalDate.of(2022, 2,2),
        LocalDate.of(2022, 2,2),
        "asdf", "zxcv"
    );

    @Test
    void adminUpdateDeliveryStatusSuccessTest() {
        Delivery spyDelivery = spy(Delivery.class);
        DeliveryStatus anotherStatus = new DeliveryStatus(2L, "zxcv");
        when(spyDelivery.getStatus()).thenReturn(deliveryStatus);
//        when(update.status()).thenReturn("zxcv");   // 이전과 다른 배송상태!
        when(deliveryRepository.findByIdFetch(anyLong())).thenReturn(Optional.of(spyDelivery));
        when(deliveryStatusRepository.findByName(anyString())).thenReturn(Optional.of(anotherStatus));

        assertThatCode(() -> service.adminUpdateDelivery(updateRequest))
            .doesNotThrowAnyException();

        verify(deliveryRepository, times(1)).findByIdFetch(anyLong());
        verify(deliveryStatusRepository, times(1)).findByName(anyString());
        verify(spyDelivery, times(1)).setStatus(anotherStatus);
        verify(spyDelivery, times(1)).setInvoiceNumber(any());
    }

    @Test
    void userUpdateDeliveryFailTest() {
        when(deliveryRepository.findByIdFetch(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() ->service.userUpdateDelivery(updateRequest))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(deliveryRepository, times(1)).findByIdFetch(anyLong());
    }

//    @Test
//    void userUpdateDeliveryStatusCheckFailTest() {
//        when(delivery.getStatus()).thenReturn(deliveryStatus);
//        when(deliveryRepository.findByIdFetch(anyLong())).thenReturn(Optional.of(delivery));
//
//        assertThatCode(() -> service.userUpdateDelivery(updateRequest))
//            .doesNotThrowAnyException();
//
//        verify(delivery, times(1)).setReceivedDate(updateRequest.receivedDate());
//    }

//    @Test
//    void userUpdateDeliverySuccessTest() {
//        when(delivery.getStatus()).thenReturn(deliveryStatus);
//        when(deliveryRepository.findByIdFetch(anyLong())).thenReturn(Optional.of(delivery));
//
//        assertThatCode(() -> service.userUpdateDelivery(updateRequest))
//            .doesNotThrowAnyException();
//
//        verify(delivery, times(1)).setReceivedDate(updateRequest.receivedDate());
//    }

}
