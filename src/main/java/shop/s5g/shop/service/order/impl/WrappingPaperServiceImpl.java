package shop.s5g.shop.service.order.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.wrappingpaper.WrappingPaperRequestDto;
import shop.s5g.shop.dto.wrappingpaper.WrappingPaperResponseDto;
import shop.s5g.shop.entity.order.WrappingPaper;
import shop.s5g.shop.exception.order.WrappingPaperDoesNotExistsException;
import shop.s5g.shop.repository.order.WrappingPaperRepository;
import shop.s5g.shop.service.order.WrappingPaperService;

@Transactional
@RequiredArgsConstructor
@Service
public class WrappingPaperServiceImpl implements WrappingPaperService {
    private final WrappingPaperRepository wrappingPaperRepository;

    @Override
    public WrappingPaperResponseDto save(WrappingPaperRequestDto request) {
        return WrappingPaperResponseDto.of(wrappingPaperRepository.save(request.toEntity()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<WrappingPaperResponseDto> getAllActivePaper() {
        return wrappingPaperRepository.queryByActive(true).stream()
            .map(WrappingPaperResponseDto::of).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<WrappingPaperResponseDto> getAllPapers() {
        return wrappingPaperRepository.findAll().stream().map(WrappingPaperResponseDto::of).toList();
    }

    @Override
    public void deactivateById(long id) {
        WrappingPaper paper = wrappingPaperRepository.findById(id).orElseThrow(() -> new WrappingPaperDoesNotExistsException(id));
        paper.setActive(false);
    }

    @Transactional(readOnly = true)
    @Override
    public WrappingPaperResponseDto getPaperById(long id) {
        return WrappingPaperResponseDto.of(
            wrappingPaperRepository.findById(id)
                .filter(WrappingPaper::isActive)
                .orElseThrow(() -> new WrappingPaperDoesNotExistsException(id))
        );
    }
}
