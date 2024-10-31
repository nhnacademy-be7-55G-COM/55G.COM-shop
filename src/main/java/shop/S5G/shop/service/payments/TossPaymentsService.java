package shop.S5G.shop.service.payments;

import shop.S5G.shop.dto.payments.TossPaymentsCancelRequestDto;
import shop.S5G.shop.dto.payments.TossPaymentsConfirmRequestDto;
import shop.S5G.shop.dto.payments.TossPaymentsDto;

public interface TossPaymentsService {

    TossPaymentsDto confirmPayment(TossPaymentsConfirmRequestDto confirmRequest);

    TossPaymentsDto cancelPayment(String paymentKey, TossPaymentsCancelRequestDto cancelRequest);
}
