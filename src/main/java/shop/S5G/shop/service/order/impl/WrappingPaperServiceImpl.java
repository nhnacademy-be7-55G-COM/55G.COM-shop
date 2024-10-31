package shop.S5G.shop.service.order.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.S5G.shop.dto.wrappingpaper.WrappingPaperRequestDto;
import shop.S5G.shop.dto.wrappingpaper.WrappingPaperResponseDto;
import shop.S5G.shop.entity.order.WrappingPaper;
import shop.S5G.shop.exception.order.WrappingPaperDoesNotExistsException;
import shop.S5G.shop.repository.order.WrappingPaperRepository;

@Transactional
@RequiredArgsConstructor
@Service
public class WrappingPaperServiceImpl implements shop.S5G.shop.service.order.WrappingPaperService {
    private final WrappingPaperRepository wrappingPaperRepository;

    @Override
    public WrappingPaperResponseDto save(WrappingPaperRequestDto request) {
        return WrappingPaperResponseDto.of(wrappingPaperRepository.save(request.toEntity()));
    }

    @Transactional(readOnly = true)
    @Override
    public List<WrappingPaperResponseDto> getAllActivePaper() {
        return wrappingPaperRepository.queryByActive(true).stream().map(WrappingPaperResponseDto::of).toList();
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
