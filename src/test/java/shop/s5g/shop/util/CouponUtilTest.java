package shop.s5g.shop.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CouponUtilTest {

    @Test
    void testUtilityClassConstructorThrowsException() throws NoSuchMethodException {

        Constructor<CouponUtil> constructor = CouponUtil.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        Throwable exception = assertThrows(InvocationTargetException.class, constructor::newInstance);

        assertInstanceOf(UnsupportedOperationException.class, exception.getCause(),
            "Expected cause to be UnsupportedOperationException");

        assertEquals("Utility class cannot be instantiated", exception.getCause().getMessage());
    }

    @Test
    void testCreateUniqueCouponNumber() {

        int totalCoupons = 1000;
        Set<String> uniqueCoupons = new HashSet<>();

        for (int i = 0; i < totalCoupons; i++) {
            String coupon = CouponUtil.createUniqueCouponNumber();

            assertEquals(15, coupon.length(), "Generated coupon length should be 15");

            uniqueCoupons.add(coupon);
        }

        assertEquals(totalCoupons, uniqueCoupons.size(), "All generated coupons should be unique");
    }

    @Test
    void testEncodeBase662() {
        byte[] input = new byte[]{(byte) 255, (byte) 128, (byte) 64, (byte) 8};

        String encoded = CouponUtil.encodeBase62(input);

        assertNotNull(encoded, "Encoded result should not be null");
        assertEquals(input.length, encoded.length(), "Encoded result length should match input length");
    }
}
