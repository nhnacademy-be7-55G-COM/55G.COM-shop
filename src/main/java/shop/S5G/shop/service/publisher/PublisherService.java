package shop.S5G.shop.service.publisher;

import jakarta.validation.Valid;
import shop.S5G.shop.dto.publisher.PublisherRequestDto;
import shop.S5G.shop.dto.publisher.PublisherResponseDto;

public interface PublisherService {
    void addPublisher(@Valid PublisherRequestDto publisherRequestDto);

    PublisherResponseDto getPublisher(@Valid Long publisherId);

    void updatePublisher(@Valid Long publisherId, @Valid PublisherRequestDto publisherRequestDto);

    void deletePublisher(@Valid Long publisherId);
}
