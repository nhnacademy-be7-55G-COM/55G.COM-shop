package shop.s5g.shop.service.publisher.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.publisher.PublisherRequestDto;
import shop.s5g.shop.dto.publisher.PublisherResponseDto;
import shop.s5g.shop.entity.Publisher;
import shop.s5g.shop.exception.publisher.PublisherAlreadyExistsException;
import shop.s5g.shop.exception.publisher.PublisherResourceNotFoundException;
import shop.s5g.shop.repository.publisher.PublisherRepository;
import shop.s5g.shop.service.publisher.PublisherService;

@Service
@Transactional
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;
    @Autowired
    public PublisherServiceImpl(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    //출판사 등록
    @Override
    public void addPublisher(PublisherRequestDto publisherRequestDto) {
        if(publisherRepository.existsByNameAndActive(publisherRequestDto.name(), true)) {
            throw new PublisherAlreadyExistsException("이미 존재하는 출판사 입니다.");
        }
        Publisher publisher = new Publisher(publisherRequestDto.name(), true);
        publisherRepository.save(publisher);
    }

    //출판사 조회
    @Override
    @Transactional(readOnly = true)
    public PublisherResponseDto getPublisher(Long id) {
        if(!publisherRepository.existsById(id)) {
            throw new PublisherResourceNotFoundException("해당 출판사는 존재하지 않습니다.");
        }

        return publisherRepository.getPublisher(id);
    }

    //모든 출판사 조회
    @Override
    public Page<PublisherResponseDto> getAllPublisher(Pageable pageable) {
        return publisherRepository.getAllPublisher(pageable);
    }

    //출판사 수정
    @Override
    public void updatePublisher(Long publisherId, PublisherRequestDto publisherRequestDto) {

        if(!publisherRepository.existsById(publisherId)) {
            throw new PublisherResourceNotFoundException("수정할 출판사가 존재하지 않습니다.");
        }
        publisherRepository.updatePublisher(publisherId, publisherRequestDto);
    }

    //출판사 삭제
    @Override
    public void deletePublisher(Long publisherId) {
        if(!publisherRepository.existsById(publisherId)) {
            throw new PublisherResourceNotFoundException("삭제할 출판사가 존재하지 않습니다.");
        }
        publisherRepository.deletePublisher(publisherId);
    }
}