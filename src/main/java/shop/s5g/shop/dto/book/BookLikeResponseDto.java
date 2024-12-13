package shop.s5g.shop.dto.book;

public record BookLikeResponseDto(
        //도서 제목, 도서 가격, 작가 이름, 출판사, 도서 상태, 도서 이미지
        Long bookId,
        String title,
        Long price,
        String publisher,
        String status
) {
}
