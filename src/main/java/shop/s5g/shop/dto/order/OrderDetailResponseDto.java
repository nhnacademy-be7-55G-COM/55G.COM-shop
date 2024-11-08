package shop.s5g.shop.dto.order;

// order_detail 을 위한 dto
public record OrderDetailResponseDto(
    long bookId,
    String bookTitle,
    String bookThumbnail,
    String wrappingPaperName,
    long orderDetailId,
    String orderDetailType,
    int quantity,
    long totalPrice,
    int accumulationPrice
) {}
