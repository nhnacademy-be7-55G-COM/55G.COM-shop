package shop.s5g.shop.controller.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
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

    @Test
    @DisplayName("POST /api/shop/member - Register a new member")
    void registerMember() throws Exception {
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
}
