package shop.S5G.shop.service.publisher;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.S5G.shop.dto.publisher.PublisherRequestDto;
import shop.S5G.shop.exception.publisher.PublisherAlreadyExistsException;
import shop.S5G.shop.exception.publisher.PublisherResourceNotFoundException;
import shop.S5G.shop.repository.publisher.PublisherRepository;
import shop.S5G.shop.service.publisher.impl.PublisherServiceImpl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PublisherServiceTest {

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private PublisherServiceImpl publisherService;

    /**
     * 출판사 등록 test
     */
    @Test
    void addPublisher() {
        //given
        PublisherRequestDto publisher = new PublisherRequestDto("한빛", true);
        when(publisherRepository.existsByNameAndActive("한빛", true)).thenReturn(true);
        //when
        assertThatThrownBy(()-> publisherService.addPublisher(publisher)).isInstanceOf(PublisherAlreadyExistsException.class);
        //then
        verify(publisherRepository, never()).save(any());
    }

    /**
     * 출판사 삭제 test
     */
    @Test
    void deletePublisher() {
        when(publisherRepository.existsById(eq(1L))).thenReturn(false);
        assertThatThrownBy(() -> publisherService.deletePublisher(1L)).isInstanceOf(PublisherResourceNotFoundException.class);
        verify(publisherRepository, times(1)).existsById(any());
        verify(publisherRepository, never()).deletePublisher(any());
    }
}
