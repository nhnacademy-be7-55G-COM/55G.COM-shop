package shop.S5G.shop.service.payments.impl;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import shop.S5G.shop.adapter.TossPaymentsAdapter;
import shop.S5G.shop.dto.payments.TossPaymentsCancelRequestDto;
import shop.S5G.shop.dto.payments.TossPaymentsConfirmRequestDto;
import shop.S5G.shop.dto.payments.TossPaymentsDto;
import shop.S5G.shop.exception.EssentialDataNotFoundException;

@RequiredArgsConstructor
@Service("tossPaymentsService")
public class TossPaymentsServiceImpl implements shop.S5G.shop.service.payments.TossPaymentsService {
    private final TossPaymentsAdapter adapter;
    private ObjectProvider<Map<String, Object>> headersProvider;

    @Autowired
    public void setHeadersProvider(@Qualifier("tossPaymentsAuthHeader") ObjectProvider<Map<String, Object>> headersProvider) {
        this.headersProvider = headersProvider;
    }

    @Override
    public TossPaymentsDto confirmPayment(TossPaymentsConfirmRequestDto confirmRequest) {
        Map<String, Object> headers = headersProvider.getIfAvailable();
//            // TODO: 적절한 예외로 바꾸기!
        if (headers == null)
            throw new EssentialDataNotFoundException("headersProvider error");
        headers.put("Content-Type", MediaType.APPLICATION_JSON);
        ResponseEntity<TossPaymentsDto> response = adapter.confirm(headers, confirmRequest);
        // 근데 어차피 feignClient 는 20x, 30x가 아니면 알아서 예외를 반환해버림..
//        if (!response.getStatusCode().is2xxSuccessful()) {
//            throw new RuntimeException();
//        }
        return response.getBody();
    }

    @Override
    public TossPaymentsDto cancelPayment(String paymentKey,
        TossPaymentsCancelRequestDto cancelRequest) {
        Map<String, Object> headers = headersProvider.getIfAvailable();
        if (headers == null)
            throw new EssentialDataNotFoundException("headersProvider error");
        headers.put("Content-Type", MediaType.APPLICATION_JSON);
        return adapter.cancel(headers, paymentKey, cancelRequest).getBody();
    }
}
