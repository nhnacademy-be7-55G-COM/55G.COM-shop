package shop.s5g.shop.util;

import java.util.UUID;

public class CouponUtil {

    private static final String BASE62 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    // 객체 생성 방지
    private CouponUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 쿠폰 번호 랜덤 생성
     * @return String
     */
    public static String createUniqueCouponNumber() {
        String uuid = UUID.randomUUID().toString();
        return encodeBase62(uuid.getBytes()).substring(0, 15);
    }

    /**
     * Base62 인코딩
     * @param input 바이트 배열
     * @return String
     */
    public static String encodeBase62(byte[] input) {
        StringBuilder result = new StringBuilder();
        for (byte b : input) {
            result.append(BASE62.charAt(((b & 0xFF) % BASE62.length())));
        }
        return result.toString();
    }
}
