package shop.S5G.shop.repository.publisher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import shop.S5G.shop.config.QueryFactoryConfig;
import shop.S5G.shop.dto.publisher.PublisherRequestDto;
import shop.S5G.shop.dto.publisher.PublisherResponseDto;
import shop.S5G.shop.entity.Publisher;
import shop.S5G.shop.exception.publisher.PublisherResourceNotFoundException;
import shop.S5G.shop.repository.publisher.qdsl.impl.PublisherQuerydslRepositoryImpl;

@DataJpaTest
@Import(QueryFactoryConfig.class)
public class PublisherRepositoryTest {

    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private PublisherQuerydslRepositoryImpl publisherQuerydslRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    /**
     * 출판사 등록 test
     */
    @Test
    @DisplayName("출판사 등록 test")
    void addPublisherTest() {
        Publisher publisher = new Publisher("한빛", true);
        Publisher save = publisherRepository.save(publisher);
        Assertions.assertEquals("한빛",save.getName());
    }

    /**
     * 출판사 조회 test
     */
    @Test
    @DisplayName("출판사 조회 test")
    void getPublisherTest() {
        Publisher publisher = new Publisher("한빛", true);
        Publisher save = publisherRepository.save(publisher);
        PublisherResponseDto publisherResponseDto = publisherQuerydslRepository.getPublisher(save.getId());
        Assertions.assertEquals("한빛", publisherResponseDto.name());
    }

    /**
     * 출판사 수정 test
     */
    @Test
    @DisplayName("출판사 수정 test")
    void updatePublisherTest() {
        Publisher publisher = new Publisher("한빛", true);
        PublisherRequestDto newPublisher = new PublisherRequestDto("창비", true);

        Publisher save = publisherRepository.save(publisher);
        publisherRepository.updatePublisher(save.getId(), newPublisher);

        //영속성 컨텍스트를 갱신하여 변경 사항을 반영
        testEntityManager.flush();
        testEntityManager.clear();

        Publisher publisherResult = publisherRepository.findById(save.getId())
                .orElseThrow(() -> new PublisherResourceNotFoundException("없는 출판사 입니다."));
        Assertions.assertEquals("창비", publisherResult.getName());
    }

    /**
     * 출판사 삭제 test
     */
    @Test
    @DisplayName("출판사 삭제(비활성화) test")
    void deletePublisherTest() {
        Publisher publisher = new Publisher("한빛", true);
        Publisher save = publisherRepository.save(publisher);
        //출판사 비활성화
        publisherRepository.deletePublisher(save.getId());
        //영속성 컨텍스트를 갱신하여 변경사항 반영
        testEntityManager.flush();
        testEntityManager.clear();

        //DB에서 엔티티를 다시 조회하여 비활성화 확인
        Publisher newPublisher = publisherRepository.findById(save.getId())
                .orElseThrow(() -> new PublisherResourceNotFoundException("출판사는 없습니다."));

        Assertions.assertEquals(false, newPublisher.isActive());
    }

}
