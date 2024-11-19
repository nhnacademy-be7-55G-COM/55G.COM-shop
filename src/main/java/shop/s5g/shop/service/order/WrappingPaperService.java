package shop.s5g.shop.service.order;

import java.util.List;
import shop.s5g.shop.dto.wrappingpaper.WrappingPaperRequestDto;
import shop.s5g.shop.dto.wrappingpaper.WrappingPaperResponseDto;

public interface WrappingPaperService {

    WrappingPaperResponseDto save(WrappingPaperRequestDto request);

    List<WrappingPaperResponseDto> getAllActivePaper();

    List<WrappingPaperResponseDto> getAllPapers();

    void deactivateById(long id);

    WrappingPaperResponseDto getPaperById(long id);
}
