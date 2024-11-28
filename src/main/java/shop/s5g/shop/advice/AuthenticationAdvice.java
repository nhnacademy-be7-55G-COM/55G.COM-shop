package shop.s5g.shop.advice;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import shop.s5g.shop.exception.ForbiddenResourceException;

@Aspect
@Component
public class AuthenticationAdvice {
    @Pointcut("execution(* shop.s5g.shop.controller..*(.., @org.springframework.security.core.annotation.AuthenticationPrincipal (*), ..)))")
    public void authRequired() {}

    @Before("authRequired()")
    public void checkAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            throw new ForbiddenResourceException("Forbidden Resource");
        }
    }
}
