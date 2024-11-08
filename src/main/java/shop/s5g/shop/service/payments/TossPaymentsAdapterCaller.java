package shop.s5g.shop.service.payments;

import shop.s5g.shop.dto.payments.TossPaymentsCancelRequestDto;
import shop.s5g.shop.dto.payments.TossPaymentsConfirmRequestDto;
import shop.s5g.shop.dto.payments.TossPaymentsDto;

public interface TossPaymentsAdapterCaller {

    TossPaymentsDto confirmPayment(TossPaymentsConfirmRequestDto confirmRequest);

    TossPaymentsDto cancelPayment(String paymentKey, TossPaymentsCancelRequestDto cancelRequest);
}
