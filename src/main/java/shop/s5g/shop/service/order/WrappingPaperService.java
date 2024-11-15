package shop.s5g.shop.service.order;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.wrappingpaper.WrappingPaperRequestDto;
import shop.s5g.shop.dto.wrappingpaper.WrappingPaperResponseDto;

public interface WrappingPaperService {

    WrappingPaperResponseDto save(WrappingPaperRequestDto request);

    @Transactional(readOnly = true)
    List<WrappingPaperResponseDto> getAllActivePaper();

    List<WrappingPaperResponseDto> getAllPapers();

    void deactivateById(long id);

    @Transactional(readOnly = true)
    WrappingPaperResponseDto getPaperById(long id);
}
