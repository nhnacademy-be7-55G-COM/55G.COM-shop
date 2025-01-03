package shop.s5g.shop.filter;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import shop.s5g.shop.adapter.AuthAdapter;
import shop.s5g.shop.dto.auth.UserDetailResponseDto;
import shop.s5g.shop.exception.AuthenticationException;
import shop.s5g.shop.exception.member.MemberNotFoundException;
import shop.s5g.shop.security.ShopMemberDetail;
import shop.s5g.shop.service.member.MemberService;
import shop.s5g.shop.util.JwtUtil;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final AuthAdapter authAdapter;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        log.trace("Request incoming... :{}", request.getServletPath());
        try {
            if (request.getHeader(HttpHeaders.AUTHORIZATION) == null) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = request.getHeader(HttpHeaders.AUTHORIZATION).substring(7);

            if (token.equals("ANONYMOUS")) {
                filterChain.doFilter(request, response);
                return;
            }

            Map<String, Object> claims = JwtUtil.decodePayload(token);
            String uuid = (String) claims.get("id");

            ResponseEntity<UserDetailResponseDto> responseEntity = authAdapter.getUserDetails(uuid);
            if (!responseEntity.getStatusCode().is2xxSuccessful()
                || responseEntity.getBody() == null) {
                throw new IllegalArgumentException();
            }

            UserDetailResponseDto responseDto = responseEntity.getBody();
            String role = responseDto.role();
            ShopMemberDetail detail = null;

            if (role.equals("ROLE_MEMBER")) {
                long customerId = memberService.getMember(responseDto.username()).getId();
                detail = new ShopMemberDetail(responseDto.username(),
                    responseDto.role(), customerId);
            } else if (role.equals("ROLE_ADMIN")) {
                detail = new ShopMemberDetail(responseDto.username(), responseDto.role(), null);
            }

            AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                detail, null, Collections.singletonList(new SimpleGrantedAuthority(
                responseDto.role()))
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        } catch (AuthenticationException | MemberNotFoundException | MalformedJwtException e) {
            // do nothing
        }
        filterChain.doFilter(request, response);
    }
}
