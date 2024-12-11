package shop.s5g.shop.controller.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.dto.coupon.MemberRegisteredEvent;
import shop.s5g.shop.dto.customer.CustomerUpdateRequestDto;
import shop.s5g.shop.dto.member.IdCheckResponseDto;
import shop.s5g.shop.dto.member.LoginResponseDto;
import shop.s5g.shop.dto.member.MemberDetailResponseDto;
import shop.s5g.shop.dto.member_grade.MemberGradeResponseDto;
import shop.s5g.shop.dto.member_status.MemberStatusResponseDto;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.security.ShopMemberDetail;
import shop.s5g.shop.service.member.CustomerService;
import shop.s5g.shop.service.member.MemberService;

@AutoConfigureRestDocs
@WebMvcTest(
    value = MemberController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import(TestSecurityConfig.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private ApplicationEventPublisher eventPublisher;

    @Test
    @DisplayName("POST /api/shop/member - Register a new member")
    void registerMember() throws Exception {

        doNothing().when(eventPublisher).publishEvent(any(MemberRegisteredEvent.class));

        mockMvc.perform(post("/api/shop/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{ \"name\": \"JohnDoe\", \"email\": \"john.doe@example.com\", \"loginId\": \"johnny\", \"password\": \"securepassword\", \"phoneNumber\": \"12345678901\", \"birthDate\": \"19900101\" }"))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("member-register",
                requestFields(
                    fieldWithPath("name").type(JsonFieldType.STRING)
                        .description("Name of the member")
                        .attributes(key("constraints").value("NotNull, length 1-30")),
                    fieldWithPath("email").type(JsonFieldType.STRING)
                        .description("Email of the member")
                        .attributes(key("constraints").value("NotNull, email, length 4-300")),
                    fieldWithPath("loginId").type(JsonFieldType.STRING)
                        .description("Login ID of the member")
                        .attributes(key("constraints").value("NotNull, length 4-30")),
                    fieldWithPath("password").type(JsonFieldType.STRING)
                        .description("Password of the member")
                        .attributes(key("constraints").value("NotNull, length 4-255")),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                        .description("Phone number of the member")
                        .attributes(key("constraints").value("NotNull, length 11")),
                    fieldWithPath("birthDate").type(JsonFieldType.STRING)
                        .description("Birth date of the member")
                        .attributes(key("constraints").value("NotNull, length 8"))
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("Response Message")
                )));
    }

    @Test
    @DisplayName("POST /api/shop/member - Bad Request on Validation Error")
    void registerMemberBadRequest() throws Exception {

        doNothing().when(eventPublisher).publishEvent(any(MemberRegisteredEvent.class));

        mockMvc.perform(post("/api/shop/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{ \"name\": \"\", \"email\": \"invalidEmail\", \"loginId\": \"\", \"password\": \"\", \"phoneNumber\": \"1234567890\", \"birthDate\": \"\" }")) // Invalid input
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andDo(document("member-register-bad-request",
                requestFields(
                    fieldWithPath("name").type(JsonFieldType.STRING)
                        .description("Name of the member")
                        .attributes(key("constraints").value("NotNull, length 1-30")),
                    fieldWithPath("email").type(JsonFieldType.STRING)
                        .description("Email of the member")
                        .attributes(key("constraints").value("NotNull, email, length 1-300")),
                    fieldWithPath("loginId").type(JsonFieldType.STRING)
                        .description("Login ID of the member")
                        .attributes(key("constraints").value("NotNull, length 4-30")),
                    fieldWithPath("password").type(JsonFieldType.STRING)
                        .description("Password of the member")
                        .attributes(key("constraints").value("NotNull, length 4-30")),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                        .description("Phone number of the member")
                        .attributes(key("constraints").value("NotNull, length 11")),
                    fieldWithPath("birthDate").type(JsonFieldType.STRING)
                        .description("Birth date of the member")
                        .attributes(key("constraints").value("NotNull, length 8"))
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("Response Message")
                )));
        verify(memberService, never()).saveMember(any());
    }

    @Test
    void getMember_shouldReturnMemberDetail() throws Exception {
        // Given
        MemberDetailResponseDto responseDto = new MemberDetailResponseDto(
            1L,
            new MemberStatusResponseDto(1L, "ACTIVE"),
            new MemberGradeResponseDto(1L, "일반", 1000, 1),
            "testUser",
            "password123",
            "19900101",
            "Test User",
            "test@example.com",
            "01012345678",
            LocalDateTime.of(2023, 1, 1, 12, 0),
            null,
            100L,
            List.of()
        );

        // Mock SecurityContext
        ShopMemberDetail memberDetail = new ShopMemberDetail("testUser", "ROLE_USER", 1L);
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(memberDetail, null,
                memberDetail.getAuthorities())
        );

        when(memberService.getMemberDto("testUser")).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(get("/api/shop/member"))
            .andExpect(status().isOk())
            .andDo(print())  // 실제 응답 출력 확인
            .andDo(document("get-member-detail",
                responseFields(
                    fieldWithPath("customerId").type(JsonFieldType.NUMBER)
                        .description("Customer ID"),
                    fieldWithPath("status.memberStatusId").type(JsonFieldType.NUMBER)
                        .description("Member status ID"),
                    fieldWithPath("status.typeName").type(JsonFieldType.STRING)
                        .description("Status type"),
                    fieldWithPath("grade.memberGradeId").type(JsonFieldType.NUMBER)
                        .description("Member grade ID"),
                    fieldWithPath("grade.gradeName").type(JsonFieldType.STRING)
                        .description("Grade name"),
                    fieldWithPath("grade.gradeCondition").type(JsonFieldType.NUMBER)
                        .description("Grade condition"),
                    fieldWithPath("grade.point").type(JsonFieldType.NUMBER)
                        .description("Points for the grade"),
                    fieldWithPath("loginId").type(JsonFieldType.STRING).description("Login ID"),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("Password"),
                    fieldWithPath("birth").type(JsonFieldType.STRING).description("Birth date"),
                    fieldWithPath("name").type(JsonFieldType.STRING).description("Member name"),
                    fieldWithPath("email").type(JsonFieldType.STRING).description("Member email"),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                        .description("Member phone number"),
                    fieldWithPath("createdAt").type(JsonFieldType.STRING)
                        .description("Account creation time"),
                    fieldWithPath("latestLoginAt").type(JsonFieldType.STRING).optional()
                        .description("Last login time"),
                    fieldWithPath("point").type(JsonFieldType.NUMBER).description("Member points"),
                    fieldWithPath("addresses").type(JsonFieldType.ARRAY)
                        .description("List of addresses")
                )
            ));
        verify(memberService).getMemberDto("testUser");
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"USER"})
    void getMemberLoginInfo_shouldReturnLoginInfo() throws Exception {
        // Given
        LoginResponseDto loginResponseDto = new LoginResponseDto("testUser", "password123");

        when(memberService.getLoginDto("testUser")).thenReturn(loginResponseDto);

        // When & Then
        mockMvc.perform(get("/api/shop/member/login/{loginId}", "testUser"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.loginId").value("testUser"))
            .andExpect(jsonPath("$.password").value("password123"))
            .andDo(print())
            .andDo(document("get-member-login-info",
                responseFields(
                    fieldWithPath("loginId").type(JsonFieldType.STRING).description("Login ID"),
                    fieldWithPath("password").type(JsonFieldType.STRING).description("Password")
                )));

        verify(memberService).getLoginDto("testUser");
    }

    @Test
    void changePassword_shouldChangePassword() throws Exception {
        String changePasswordJson = """
            {
                "oldPassword": "oldPassword123",
                "newPassword": "newPassword123"
            }
            """;

        // Custom SecurityContext 설정
        ShopMemberDetail memberDetail = new ShopMemberDetail("testUser", "ROLE_USER", 1L);
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(memberDetail, null,
                memberDetail.getAuthorities())
        );

        // When & Then
        mockMvc.perform(post("/api/shop/member/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(changePasswordJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("비밀번호 변경 성공"))
            .andDo(print())
            .andDo(document("change-password",
                requestFields(
                    fieldWithPath("oldPassword").type(JsonFieldType.STRING)
                        .description("Old password"),
                    fieldWithPath("newPassword").type(JsonFieldType.STRING)
                        .description("New password")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("Response message")
                )));

        verify(memberService).changePassword(1L, "oldPassword123", "newPassword123");
    }

    @Test
    void updateMember_shouldUpdateMemberInfo() throws Exception {
        // Given
        ShopMemberDetail memberDetail = new ShopMemberDetail("testUser", "ROLE_USER", 1L);
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(memberDetail, null,
                memberDetail.getAuthorities())
        );

        CustomerUpdateRequestDto requestDto = new CustomerUpdateRequestDto("New Name",
            "test@example.com", "01012345678");

        // When & Then
        mockMvc.perform(put("/api/shop/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "New Name",
                        "email": "test@example.com",
                        "phoneNumber": "01012345678"
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("회원 정보가 변경되었습니다."))
            .andDo(print())
            .andDo(document("update-member",
                requestFields(
                    fieldWithPath("name").type(JsonFieldType.STRING).description("Updated name"),
                    fieldWithPath("email").type(JsonFieldType.STRING).description("Updated email"),
                    fieldWithPath("phoneNumber").type(JsonFieldType.STRING)
                        .description("Updated phone number")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("Response message")
                )));

        verify(customerService).updateCustomer(1L, requestDto);
    }

    @Test
    void deleteMember_shouldDeleteMember() throws Exception {
        // Given
        ShopMemberDetail memberDetail = new ShopMemberDetail("testUser", "ROLE_USER", 1L);
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(memberDetail, null,
                memberDetail.getAuthorities())
        );

        // When & Then
        mockMvc.perform(delete("/api/shop/member"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("탈퇴 처리가 완료됐습니다."))
            .andDo(print())
            .andDo(document("delete-member",
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("Response message")
                )));

        verify(memberService).deleteById(1L);
        verify(customerService).deleteCustomer(1L);
    }

    @Test
    void checkId_shouldReturnIdCheckResponse() throws Exception {
        // Given
        String loginId = "testUser";
        IdCheckResponseDto responseDto = new IdCheckResponseDto(true);

        when(memberService.isExistsByLoginId(loginId)).thenReturn(responseDto);

        // When & Then
        mockMvc.perform(post("/api/shop/checkId/{loginId}", loginId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.isExists").value(true))
            .andDo(print())
            .andDo(document("check-id",
                responseFields(
                    fieldWithPath("isExists").type(JsonFieldType.BOOLEAN)
                        .description("Whether the login ID exists")
                )));

        verify(memberService).isExistsByLoginId(loginId);
    }

    @Test
    void linkPayco_shouldLinkPaycoAccount() throws Exception {
        // Given
        String paycoId = "payco123";
        ShopMemberDetail memberDetail = new ShopMemberDetail("testUser", "ROLE_USER", 1L);
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(memberDetail, null,
                memberDetail.getAuthorities())
        );

        // When & Then
        mockMvc.perform(post("/api/shop/member/payco/link")
                .param("payco_id", paycoId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("연동 성공"));

        verify(memberService).linkAccountByPaycoId(1L, paycoId);
    }
}