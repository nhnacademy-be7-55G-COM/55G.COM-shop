package shop.s5g.shop.config;

import java.util.Collections;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import shop.s5g.shop.annotation.WithCustomMockUser;
import shop.s5g.shop.security.ShopMemberDetail;

public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<WithCustomMockUser> {
    @Override
    public SecurityContext createSecurityContext(WithCustomMockUser annotation) {
        String loginId = annotation.loginId();
        String role = annotation.role();
        long customerId = annotation.customerId();
        AbstractAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            new ShopMemberDetail(loginId, role, customerId), null, Collections.singletonList(new SimpleGrantedAuthority(role))
        );
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(token);

        return context;
    }
}
