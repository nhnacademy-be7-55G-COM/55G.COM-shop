package shop.s5g.shop.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
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
import shop.s5g.shop.dto.wrappingpaper.WrappingPaperRequestDto;
import shop.s5g.shop.entity.order.WrappingPaper;
import shop.s5g.shop.exception.ResourceNotFoundException;
import shop.s5g.shop.repository.order.WrappingPaperRepository;
import shop.s5g.shop.service.order.impl.WrappingPaperServiceImpl;

@ExtendWith(MockitoExtension.class)
class WrappingPaperServiceImplTest {
    @Mock
    WrappingPaperRepository wrappingPaperRepository;

    @InjectMocks
    WrappingPaperServiceImpl service;

    WrappingPaperRequestDto request = new WrappingPaperRequestDto(
        "테스트", 2000, "test.png"
    );
    WrappingPaper paper = new WrappingPaper(
        1L, request.name(), request.price(), true, request.imageName()
    );

    @Test
    void saveTest() {
        when(wrappingPaperRepository.save(any())).thenReturn(paper);

        assertThat(service.save(request))
            .hasFieldOrPropertyWithValue("name", request.name())
            .hasFieldOrPropertyWithValue("active", true);

        verify(wrappingPaperRepository, times(1)).save(any());
    }

    @Test
    void getAllActivePaperTest() {
        when(wrappingPaperRepository.queryByActive(true)).thenReturn(List.of());

        assertThatCode(() -> service.getAllActivePaper())
            .doesNotThrowAnyException();
    }

    @Test
    void deactivateByIdSuccessTest() {
        WrappingPaper mockPaper = mock(WrappingPaper.class);
        when(wrappingPaperRepository.findById(anyLong())).thenReturn(Optional.of(mockPaper));

        assertThatCode(() -> service.deactivateById(1L))
            .doesNotThrowAnyException();

        verify(wrappingPaperRepository, times(1)).findById(1L);
        verify(mockPaper, times(1)).setActive(false);
    }
    @Test
    void deactivateByIdFailTest() {
        when(wrappingPaperRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.deactivateById(1L))
            .isInstanceOf(ResourceNotFoundException.class);

        verify(wrappingPaperRepository, times(1)).findById(1L);
    }
}
