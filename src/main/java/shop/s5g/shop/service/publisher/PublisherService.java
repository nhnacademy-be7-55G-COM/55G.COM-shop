package shop.s5g.shop.service.publisher;

import jakarta.validation.Valid;
import shop.s5g.shop.dto.publisher.PublisherRequestDto;
import shop.s5g.shop.dto.publisher.PublisherResponseDto;

import java.util.List;

public interface PublisherService {
    void addPublisher(@Valid PublisherRequestDto publisherRequestDto);

    PublisherResponseDto getPublisher(@Valid Long id);

    void updatePublisher(@Valid Long publisherId, @Valid PublisherRequestDto publisherRequestDto);

    void deletePublisher(@Valid Long publisherId);

    List<PublisherResponseDto> getAllPublisher();
}
