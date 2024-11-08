package shop.s5g.shop.dto.order;

// order_detail 을 위한 dto
public record OrderDetailWithBookResponseDto(
    long bookId,
    String bookTitle,
// TODO: book thumbnail 다시 물어보기
//    String bookThumbnail,
    String wrappingPaperName,
    long orderDetailId,
    String orderDetailType,
    int quantity,
    long totalPrice,
    int accumulationPrice
) {}
