package shop.s5g.shop.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import shop.s5g.shop.exception.AuthenticationException;

public class JwtUtil {

    private JwtUtil() {

    }

    public static String decodeJwtLoginId(HttpServletRequest request) {
        return getClaimsFromRequest(request).get("loginId").toString();
    }

    public static Claims getClaimsFromRequest(HttpServletRequest request) {
        String token = checkAuthHeader(request);
        if (token == null) {
            throw new AuthenticationException();
        }
        return Jwts.parser().build().parseSignedClaims(token).getPayload();
    }

    public static String checkAuthHeader(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.substring(7);
    }

    public static Map<String, Object> decodePayload(String jwt) throws IOException {
        String[] parts = jwt.split("\\.");
        if (parts.length < 2) {
            throw new IllegalArgumentException();
        }
        byte[] decode = Base64.getUrlDecoder().decode(parts[1]);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(decode, Map.class);
    }
}
