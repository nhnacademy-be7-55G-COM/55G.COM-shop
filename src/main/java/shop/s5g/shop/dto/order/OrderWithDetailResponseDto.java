package shop.s5g.shop.dto.order;

import java.time.LocalDateTime;

// 주문 목록에 표기하기 위한 dto
// 알라딘 주문목록을 참고하였음.
public record OrderWithDetailResponseDto(
    long orderId,
    LocalDateTime orderedAt,
    long netPrice,
    long totalPrice,
    // 리스트에서 노출시킬 대표 디테일("'예시타이틀' 2권 외...")
    String representTitle,
    long totalKind,      // 종류(총 N종)
    int totalQuantity,   // 양(총 N권),
    String uuid
) {}
