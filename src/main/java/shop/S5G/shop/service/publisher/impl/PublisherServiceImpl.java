package shop.S5G.shop.service.publisher.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.S5G.shop.dto.publisher.PublisherRequestDto;
import shop.S5G.shop.dto.publisher.PublisherResponseDto;
import shop.S5G.shop.entity.Publisher;
import shop.S5G.shop.exception.publisher.PublisherResourceNotFoundException;
import shop.S5G.shop.repository.publisher.PublisherRepository;
import shop.S5G.shop.service.publisher.PublisherService;

@Service
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;
    @Autowired
    public PublisherServiceImpl(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    //출판사 등록
    @Override
    public void addPublisher(PublisherRequestDto publisherRequestDto) {
        Publisher publisher = new Publisher(publisherRequestDto.name(), publisherRequestDto.active());
        publisherRepository.save(publisher);
    }

    //출판사 조회
    @Override
    public PublisherResponseDto getPublisher(Long publisherId) {
        Publisher publisher = publisherRepository.findById(publisherId).orElseThrow(() -> new PublisherResourceNotFoundException("해당 출판사는 없습니다."));
        return new PublisherResponseDto(publisher.getName(), publisher.isActive());
    }

    //출판사 수정
    @Override
    public void updatePublisher(Long publisherId, PublisherRequestDto publisherRequestDto) {
        Publisher publisher = publisherRepository.findById(publisherId).orElseThrow(() -> new PublisherResourceNotFoundException("해당 출판사는 없습니다."));
        publisher.setName(publisherRequestDto.name());
        publisher.setActive(publisherRequestDto.active());
        publisherRepository.save(publisher);
    }

    //출판사 삭제
    @Override
    public void deletePublisher(Long publisherId) {
        Publisher publisher = publisherRepository.findById(publisherId).orElseThrow(() -> new PublisherResourceNotFoundException("해당 출판사는 없습니다."));
        publisher.setActive(false);
        publisherRepository.save(publisher);
    }
}
