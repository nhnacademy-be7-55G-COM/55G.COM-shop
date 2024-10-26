package shop.S5G.shop.dto.order;

// order_detail 을 위한 dto
public record OrderDetailResponseDto(
    long bookId,
    String bookTitle,
    String wrappingPaper,
    String orderDetailType,
    int quantity,
    long totalPrice,
    int accumulationPrice
) {}
