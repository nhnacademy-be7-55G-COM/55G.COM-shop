package shop.s5g.shop.test;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import shop.s5g.shop.annotation.WithCustomMockUser;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.controller.TestController;
import shop.s5g.shop.filter.JwtAuthenticationFilter;

@WebMvcTest(
    value = TestController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import(TestSecurityConfig.class)
class SecurityTest {
    @Autowired
    MockMvc mvc;

    @Test
    @WithCustomMockUser(loginId = "123", role = "ROLE_MEMBER", customerId = 2L)
    void securityTest() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/shop/test/auth"))
            .andDo(print());
    }
}
