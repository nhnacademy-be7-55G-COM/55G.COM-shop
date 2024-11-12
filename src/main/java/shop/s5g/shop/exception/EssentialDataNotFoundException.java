package shop.s5g.shop.exception;

// 서비스 제공에 필수적인 정적 데이터가 존재하지 않을 경우.
// 멤버 등급이라던가.. 배송 타입이라던가..
public class EssentialDataNotFoundException extends RuntimeException {
    public EssentialDataNotFoundException(String message) {
        super(message);
    }

}
