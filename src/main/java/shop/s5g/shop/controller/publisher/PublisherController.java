package shop.s5g.shop.controller.publisher;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.s5g.shop.dto.publisher.PublisherRequestDto;
import shop.s5g.shop.dto.publisher.PublisherResponseDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.exception.publisher.PublisherBadRequestException;
import shop.s5g.shop.service.publisher.PublisherService;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
public class PublisherController {

    private final PublisherService publisherService;

    @Autowired
    public PublisherController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    //출판사 등록
    @PostMapping("/publisher")
    public ResponseEntity<MessageDto> addPublisher(@Valid @RequestBody PublisherRequestDto publisherRequestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new PublisherBadRequestException("잘못된 입력입니다.");
        }
        publisherService.addPublisher(publisherRequestDto);
        return ResponseEntity.ok().body(new MessageDto("출판사 등록 성공"));
    }

    //출판사 조회
    @GetMapping("/publisher/{publisherId}")
    public ResponseEntity<PublisherResponseDto> findPublisher(@Valid @PathVariable("publisherId") Long id) {
        if (id < 1) {
            throw new PublisherBadRequestException("출판사 Id는 1보다 커야 합니다.");
        }
        PublisherResponseDto publisherResponseDto = publisherService.getPublisher(id);
        return ResponseEntity.ok().body(publisherResponseDto);
    }

    //모든 출판사 조회
    @GetMapping("/publisher")
    public ResponseEntity<List<PublisherResponseDto>> getAllPublisher() {
        return ResponseEntity.ok().body(publisherService.getAllPublisher());
    }

    //출판사 수정
    @PutMapping("/publisher/{publisherId}")
    public ResponseEntity<MessageDto> updatePublisher(@Valid @PathVariable("publisherId") Long publisherId, @Valid @RequestBody PublisherRequestDto publisherRequestDto, BindingResult bindingResult) {
        if (publisherId < 1) {
            throw new PublisherBadRequestException("출판사 id는 1보다 커야 합니다.");
        }
        if (bindingResult.hasErrors()) {
            throw new PublisherBadRequestException("잘못된 입력 입니다.");
        }
        publisherService.updatePublisher(publisherId, publisherRequestDto);
        return ResponseEntity.ok().body(new MessageDto("출판사 수정 성공"));
    }

    //출판사 삭제
    @DeleteMapping("/publisher/{publisherId}")
    public ResponseEntity<MessageDto> deletePublisher(@Valid @PathVariable("publisherId") Long publisherId) {
        if (publisherId < 1) {
            throw new PublisherBadRequestException("출판사 id는 1보다 커야 합니다.");
        }
        publisherService.deletePublisher(publisherId);
        return ResponseEntity.ok().body(new MessageDto("출판사 삭제 성공"));
    }
}