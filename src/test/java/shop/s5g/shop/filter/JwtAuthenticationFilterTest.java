package shop.s5g.shop.filter;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import shop.s5g.shop.adapter.AuthAdapter;
import shop.s5g.shop.advice.AuthenticationAdvice;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.controller.TestController;
import shop.s5g.shop.dto.auth.UserDetailResponseDto;
import shop.s5g.shop.entity.member.Member;
import shop.s5g.shop.filter.JwtAuthenticationFilterTest.FilterTestSecurityConfig;
import shop.s5g.shop.service.member.MemberService;

@WebMvcTest(value = TestController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class))
@EnableAspectJAutoProxy
@Import({FilterTestSecurityConfig.class, AuthenticationAdvice.class})
class JwtAuthenticationFilterTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    JwtAuthenticationFilter filter;

    @MockBean
    MemberService memberService;

    @MockBean
    AuthAdapter authAdapter;

    Member mockMember = mock(Member.class);

    String authorization = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJpZCI6ImJmYTZkYWJhLTc2YTEtNDUwOS1hNjI4LWI0NDA4ODg5M2FhMCIsImlhdCI6MTczMjg2MTM1NCwiZXhwIjoxNzMyODYxOTU0fQ.WfVzpmGLil6r9uBwhhA65E8-8UF11lf7od5QqGU61rw";

    @Test
    void authFilterTest() throws Exception{
        when(authAdapter.getUserDetails(anyString())).thenReturn(
            ResponseEntity.ok(new UserDetailResponseDto("test", "ROLE_MEMBER"))
        );
        when(mockMember.getId()).thenReturn(1L);
        when(memberService.getMember(anyString())).thenReturn(mockMember);


        mvc.perform(MockMvcRequestBuilders.get("/api/shop/test/auth")
            .header(HttpHeaders.AUTHORIZATION, authorization))
            .andExpect(status().isOk());
    }

    @Test
    void authFilterAnonymousTest() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/shop/test/auth")
                .header(HttpHeaders.AUTHORIZATION, "Bearer ANONYMOUS"))
            .andExpect(status().isForbidden());

        verify(authAdapter, never()).getUserDetails(anyString());
    }

    @TestConfiguration
    public static class FilterTestSecurityConfig{
        @Autowired
        JwtAuthenticationFilter filter;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            ;
            http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
        }
    }

}
