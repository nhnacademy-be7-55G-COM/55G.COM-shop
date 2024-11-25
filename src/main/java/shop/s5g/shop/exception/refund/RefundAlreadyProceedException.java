package shop.s5g.shop.exception.refund;

public class RefundAlreadyProceedException extends RuntimeException {
    public static final RefundAlreadyProceedException INSTANCE = new RefundAlreadyProceedException();

    public RefundAlreadyProceedException() {
        super("환불이 이미 진행된 상태입니다.");
    }
}
