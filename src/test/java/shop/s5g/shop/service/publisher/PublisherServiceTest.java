package shop.s5g.shop.service.publisher;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import shop.s5g.shop.dto.publisher.PublisherRequestDto;
import shop.s5g.shop.dto.publisher.PublisherResponseDto;
import shop.s5g.shop.exception.publisher.PublisherAlreadyExistsException;
import shop.s5g.shop.exception.publisher.PublisherResourceNotFoundException;
import shop.s5g.shop.repository.publisher.PublisherRepository;
import shop.s5g.shop.service.publisher.impl.PublisherServiceImpl;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublisherServiceTest {

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherServiceImpl publisherService;

    /**
     * 출판사 등록 성공 test
     */
    @Test
    void addPublisher() {
        //given
        PublisherRequestDto publisher = new PublisherRequestDto("한빛");
        when(publisherRepository.existsByNameAndActive("한빛", true)).thenReturn(true);
        //when
        assertThatThrownBy(()-> publisherService.addPublisher(publisher)).isInstanceOf(PublisherAlreadyExistsException.class);
        //then
        verify(publisherRepository, never()).save(any());
    }

    /**
     * 출판사 등록 실패 test
     */
    @Test
    @DisplayName("출판사 등록 실패 test")
    void addErrorPublisher() {
        //given
        PublisherRequestDto publisherRequestDto = new PublisherRequestDto("창비");
        when(publisherRepository.existsByNameAndActive("창비", true)).thenReturn(true);

        //when
        assertThatThrownBy(() -> publisherService.addPublisher(publisherRequestDto))
                .isInstanceOf(PublisherAlreadyExistsException.class)
                .hasMessageContaining("이미 존재하는 출판사 입니다.");

        //then
        verify(publisherRepository, never()).save(any());
    }

    /**
     * 출판사 조회 test
     */
    @Test
    @DisplayName("출판사 조회 test")
    void getPublisherTest() {
        //given
        Long publisherId = 1L;
        PublisherResponseDto mockResponse = new PublisherResponseDto(publisherId, "창비", true);

        when(publisherRepository.existsById(publisherId)).thenReturn(true);
        when(publisherRepository.getPublisher(publisherId)).thenReturn(mockResponse);

        //when
        PublisherResponseDto result = publisherService.getPublisher(publisherId);

        //then
        assertEquals("창비", result.name());
        verify(publisherRepository, times(1)).existsById(publisherId);
    }

    /**
     * 출판사 조회 실패 test
     * 존재하지 않는 출판사
     */
    @Test
    @DisplayName("존재하지 않는 출판사 test")
    void getPublisherErrorTest() {
        //given
        Long publisherId = 1L;

        when(publisherRepository.existsById(publisherId)).thenReturn(false);

        //when
        assertThatThrownBy(() -> publisherService.getPublisher(publisherId))
                .isInstanceOf(PublisherResourceNotFoundException.class)
                .hasMessageContaining("해당 출판사는 존재하지 않습니다.");

        //then
        verify(publisherRepository, never()).getPublisher(publisherId);
    }

    /**
     * 모든 출판사 조회 test
     */
    @Test
    @DisplayName("모든 출판사 조회 test")
    void getAllPublisherTest() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        List<PublisherResponseDto> mockPublishers = List.of(
                new PublisherResponseDto(1L, "창비", true),
                new PublisherResponseDto(2L, "한빛", true));

        Page<PublisherResponseDto> mockPage = new PageImpl<>(mockPublishers, pageable, mockPublishers.size());
        when(publisherRepository.getAllPublisher(pageable)).thenReturn(mockPage);

        //when
        Page<PublisherResponseDto> result = publisherService.getAllPublisher(pageable);

        //then
        assertEquals("창비", result.getContent().get(0).name());
        verify(publisherRepository, times(1)).getAllPublisher(pageable);
    }

    /**
     * 출판사 수정 test
     */
    @Test
    @DisplayName("출판사 수정 test")
    void updatePublisherTest() {
        //given
        Long publisherId = 1L;
        PublisherRequestDto requestDto = new PublisherRequestDto("수정된 출판사");

        when(publisherRepository.existsById(publisherId)).thenReturn(true);
        doNothing().when(publisherRepository).updatePublisher(publisherId, requestDto);

        //when
        publisherService.updatePublisher(publisherId, requestDto);

        //then
        verify(publisherRepository, times(1)).updatePublisher(publisherId, requestDto);
    }

    /**
     * 출판사 수정 실패 test
     * 출판사 존재하지 않음
     */
    @Test
    @DisplayName("출판사 존재하지 않음")
    void updatePublisherErrorTest() {
        //given
        Long publisherId = 1L;
        PublisherRequestDto requestDto = new PublisherRequestDto("수정된 출판사");

        when(publisherRepository.existsById(publisherId)).thenReturn(false);

        //when
        assertThatThrownBy(() -> publisherService.updatePublisher(publisherId, requestDto))
                .isInstanceOf(PublisherResourceNotFoundException.class)
                .hasMessageContaining("수정할 출판사가 존재하지 않습니다.");

        //then
        verify(publisherRepository, never()).updatePublisher(any(), any());
    }

    /**
     * 출판사 삭제 test
     */
    @Test
    @DisplayName("출판사 삭제 test")
    void deletePublisher() {
        //given
        Long publisherId = 1L;

        when(publisherRepository.existsById(1L)).thenReturn(true);
        doNothing().when(publisherRepository).deletePublisher(publisherId);

        //when
        publisherService.deletePublisher(publisherId);

        //then
        verify(publisherRepository, times(1)).existsById(any());
    }

    /**
     * 출판사 삭제 실패 test
     * 삭제할 출판사 존재하지 않음
     */
    @Test
    @DisplayName("삭제할 출판사 존재하지 않음")
    void deletePublisherErrorTest() {
        //given
        Long publisherId = 1L;

        when(publisherRepository.existsById(publisherId)).thenReturn(false);

        //when
        assertThatThrownBy(() -> publisherService.deletePublisher(publisherId))
                .isInstanceOf(PublisherResourceNotFoundException.class)
                .hasMessageContaining("삭제할 출판사가 존재하지 않습니다.");

        //then
        verify(publisherRepository, never()).deletePublisher(any());
    }
}
