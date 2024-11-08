package shop.s5g.shop.repository.publisher.qdsl;

import shop.s5g.shop.dto.publisher.PublisherRequestDto;
import shop.s5g.shop.dto.publisher.PublisherResponseDto;

public interface PublisherQuerydslRepository {
    PublisherResponseDto getPublisher(Long publisherId);
    void updatePublisher(Long publisherId, PublisherRequestDto publisherRequestDto);
    void deletePublisher(Long publisherId);
}
