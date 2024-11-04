package shop.S5G.shop.repository.publisher.qdsl;

import shop.S5G.shop.dto.publisher.PublisherRequestDto;
import shop.S5G.shop.dto.publisher.PublisherResponseDto;

public interface PublisherQuerydslRepository {
    PublisherResponseDto getPublisher(Long publisherId);
    void updatePublisher(Long publisherId, PublisherRequestDto publisherRequestDto);
    void deletePublisher(Long publisherId);
}
