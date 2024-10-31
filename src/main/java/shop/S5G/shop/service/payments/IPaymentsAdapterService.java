package shop.S5G.shop.service.payments;

import java.util.List;
import java.util.Map;
import shop.S5G.shop.dto.tag.MessageDto;

public interface IPaymentsAdapterService {
    MessageDto savePayment(Map<String, Object> requestBody);
    <T> List<T> fetchAllPaymentsByAttribute(String attribute, Object value, Class<T> returnType);
    <T> T fetchPaymentByOrderId(String orderId, Class<T> returnType);
    MessageDto cancelPaymentByOrderId(String orderId, long amount, String reason);
}
