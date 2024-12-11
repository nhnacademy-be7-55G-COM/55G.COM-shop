package shop.s5g.shop.repository.publisher.qdsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.publisher.PublisherRequestDto;
import shop.s5g.shop.dto.publisher.PublisherResponseDto;


public interface PublisherQuerydslRepository {
    PublisherResponseDto getPublisher(Long id);
    Page<PublisherResponseDto> getAllPublisher(Pageable pageable);
    void updatePublisher(Long publisherId, PublisherRequestDto publisherRequestDto);
    void deletePublisher(Long publisherId);
}
