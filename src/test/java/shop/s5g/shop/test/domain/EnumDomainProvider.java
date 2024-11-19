package shop.s5g.shop.test.domain;

import java.util.Arrays;
import java.util.List;
import shop.s5g.shop.entity.delivery.DeliveryStatus;
import shop.s5g.shop.entity.order.OrderDetailType;

public class EnumDomainProvider {
    private EnumDomainProvider() {}

    public static List<OrderDetailType> getOrderDetailTypeList() {
        return Arrays.stream(OrderDetailType.Type.values()).map(type -> new OrderDetailType(type.ordinal(), type.name())).toList();
    }

    public static List<DeliveryStatus> getDeliveryStatusList() {
        return Arrays.stream(DeliveryStatus.Type.values()).map(type -> new DeliveryStatus(type.ordinal(), type.name())).toList();
    }
}
