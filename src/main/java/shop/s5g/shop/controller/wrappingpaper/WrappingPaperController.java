package shop.s5g.shop.controller.wrappingpaper;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.wrappingpaper.WrappingPaperRequestDto;
import shop.s5g.shop.dto.wrappingpaper.WrappingPaperResponseDto;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.service.order.WrappingPaperService;

@RequiredArgsConstructor
@RequestMapping("/api/shop/wrapping-paper")
@RestController
public class WrappingPaperController {
    private final WrappingPaperService wrappingPaperService;

    @GetMapping
    public List<WrappingPaperResponseDto> fetchPapers(@RequestParam(required = false) String scope) {
        if (scope == null || scope.isEmpty())
            return wrappingPaperService.getAllActivePaper();
        else if (scope.equals("all"))
            return wrappingPaperService.getAllPapers();
        throw new BadRequestException("Request Param was wrong");
    }

    @GetMapping("/{paperId}")
    public WrappingPaperResponseDto fetchPaper(@PathVariable long paperId) {
        return wrappingPaperService.getPaperById(paperId);
    }

    @PostMapping
    public ResponseEntity<WrappingPaperResponseDto> createPaper(@Valid @RequestBody WrappingPaperRequestDto createRequest, BindingResult error) {
        if (error.hasErrors()) {
            throw new BadRequestException("Name is null");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(wrappingPaperService.save(createRequest));
    }

    @DeleteMapping("/{paperId}")
    public ResponseEntity<HttpStatus> deletePaper(@PathVariable long paperId) {
        wrappingPaperService.deactivateById(paperId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    // update는 작성하지 않음. remove 이후 새로 paper를 추가하는 것을 권장함.
    // update가 있다면 active의 의미가 퇴색될것이라 판단됨.
}
