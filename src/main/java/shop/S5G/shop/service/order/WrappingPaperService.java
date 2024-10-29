package shop.S5G.shop.service.order;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import shop.S5G.shop.dto.wrappingpaper.WrappingPaperRequestDto;
import shop.S5G.shop.dto.wrappingpaper.WrappingPaperResponseDto;

public interface WrappingPaperService {

    WrappingPaperResponseDto save(WrappingPaperRequestDto request);

    @Transactional(readOnly = true)
    List<WrappingPaperResponseDto> getAllActivePaper();

    void deactivateById(long id);

    @Transactional(readOnly = true)
    WrappingPaperResponseDto getPaperById(long id);
}
