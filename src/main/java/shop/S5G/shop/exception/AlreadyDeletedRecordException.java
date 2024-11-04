package shop.S5G.shop.exception;

/**
 * 혹시 몰라서 이미 비활성화인 데이터 삭제나, 수정할 때 던질 에러,,,?
 * 따로 쿼리문으로 처리해도 되지만 혹시 몰라서 넣어봅니다..
 */
public class AlreadyDeletedRecordException extends RuntimeException {

    public AlreadyDeletedRecordException(String message) {
        super(message);
    }
}
