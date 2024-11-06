package shop.S5G.shop.util;


import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import shop.S5G.shop.exception.AuthenticationException;

public class JwtUtil {

    public static String decodeJwtLoginId(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            throw new AuthenticationException();
        }
        String token = header.substring(7);

        return Jwts.parser().build().parseSignedClaims(token).getPayload().get("loginId")
            .toString();
    }
}
