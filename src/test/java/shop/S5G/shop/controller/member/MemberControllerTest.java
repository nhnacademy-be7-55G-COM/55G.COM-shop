package shop.S5G.shop.controller.member;

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
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import shop.S5G.shop.service.member.MemberService;

@WebMvcTest(MemberController.class)
@AutoConfigureRestDocs
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Test
    @DisplayName("POST /api/shop/member - Register a new member")
    void registerMember() throws Exception {
        // Mock the successful registration scenario
        mockMvc.perform(post("/api/shop/member")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{ \"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"loginId\": \"johnny\", \"password\": \"securepassword\", \"phoneNumber\": \"12345678901\", \"birthDate\": \"19900101\" }"))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("member-register",
                requestFields(
                    fieldWithPath("name").type(JsonFieldType.STRING)
                        .description("Name of the member")
                        .attributes(key("constraints").value("NotNull, length 1-30")),
                    fieldWithPath("email").type(JsonFieldType.STRING)
                        .description("Email of the member")
                        .attributes(key("constraints").value("NotNull, email, length 1-300")),
                    fieldWithPath("loginId").type(JsonFieldType.STRING)
                        .description("Login ID of the member")
                        .attributes(key("constraints").value("NotNull, length 1-30")),
                    fieldWithPath("password").type(JsonFieldType.STRING)
                        .description("Password of the member")
                        .attributes(key("constraints").value("NotNull, length 1-255")),
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
        // Mock a validation error scenario
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
                        .attributes(key("constraints").value("NotNull, length 1-30")),
                    fieldWithPath("password").type(JsonFieldType.STRING)
                        .description("Password of the member")
                        .attributes(key("constraints").value("NotNull, length 1-255")),
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
}
