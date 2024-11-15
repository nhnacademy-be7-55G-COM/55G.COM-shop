package shop.s5g.shop.dto;

import java.util.List;
import org.springframework.data.domain.Page;

// 기존 Page<T>를 축약하여 페이지 컨텐츠와 페이지 총 개수, 페이지 사이즈, 총 element 개수를 보내는 dto
public record PageResponseDto<T>(List<T> content, int totalPage, int pageSize, long totalElements) {
    public static <T> PageResponseDto<T> of(Page<T> page) {
        return new PageResponseDto<>(page.getContent(), page.getTotalPages(), page.getSize(), page.getTotalElements());
    }
}
