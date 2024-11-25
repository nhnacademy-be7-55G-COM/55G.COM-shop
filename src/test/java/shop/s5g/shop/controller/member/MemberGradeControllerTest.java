package shop.s5g.shop.controller.member;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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
import shop.s5g.shop.dto.member_grade.MemberGradeRequestDto;
import shop.s5g.shop.dto.member_grade.MemberGradeResponseDto;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.member.MemberGradeService;

@AutoConfigureRestDocs
@WebMvcTest(
    value = MemberGradeController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import(TestSecurityConfig.class)
class MemberGradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberGradeService memberGradeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /api/shop/member/grade - Get all member grades")
    void getMemberGradeList() throws Exception {
        List<MemberGradeResponseDto> memberGrades = List.of(
            new MemberGradeResponseDto(1L, "일반", 0, 1),
            new MemberGradeResponseDto(2L, "로얄", 100000, 3)
        );

        given(memberGradeService.getActiveGrades()).willReturn(memberGrades);

        mockMvc.perform(get("/api/shop/member/grade")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("member-grade-list",
                responseFields(
                    fieldWithPath("[].memberGradeId").type(JsonFieldType.NUMBER)
                        .description("Member Grade ID"),
                    fieldWithPath("[].gradeName").type(JsonFieldType.STRING)
                        .description("Grade Name"),
                    fieldWithPath("[].gradeCondition").type(JsonFieldType.NUMBER)
                        .description("Grade Condition"),
                    fieldWithPath("[].point").type(JsonFieldType.NUMBER).description("Grade Points")
                )));
    }

    @Test
    @DisplayName("GET /api/shop/member/grade/{gradeId} - Get member grade by ID")
    void getMemberGrade() throws Exception {
        long gradeId = 1L;
        MemberGradeResponseDto memberGrade = new MemberGradeResponseDto(gradeId, "일반", 0, 1);

        given(memberGradeService.getGradeById(gradeId)).willReturn(memberGrade);

        mockMvc.perform(get("/api/shop/member/grade/{gradeId}",
                gradeId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("member-grade-get",
                pathParameters(
                    parameterWithName("gradeId").description("Grade ID")
                ),
                responseFields(
                    fieldWithPath("memberGradeId").type(JsonFieldType.NUMBER)
                        .description("Member Grade ID"),
                    fieldWithPath("gradeName").type(JsonFieldType.STRING).description("Grade Name"),
                    fieldWithPath("gradeCondition").type(JsonFieldType.NUMBER)
                        .description("Grade Condition"),
                    fieldWithPath("point").type(JsonFieldType.NUMBER).description("Grade Points")
                )));
    }

    @Test
    @DisplayName("POST /api/shop/member/grade - Create a new member grade")
    void createMemberGrade() throws Exception {
        MemberGradeRequestDto requestDto = new MemberGradeRequestDto("로얄", 100000, 3);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(post("/api/shop/member/grade")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(document("member-grade-create",
                requestFields(
                    fieldWithPath("gradeName").type(JsonFieldType.STRING).description("Grade Name")
                        .attributes(key("constraints").value("NotNull, length 1-15")),
                    fieldWithPath("gradeCondition").type(JsonFieldType.NUMBER)
                        .description("Condition for Grade")
                        .attributes(key("constraints").value("NotNull")),
                    fieldWithPath("point").type(JsonFieldType.NUMBER)
                        .description("Points associated with Grade")
                        .attributes(key("constraints").value("NotNull"))
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("Response Message")
                )));
    }

    @Test
    @DisplayName("PUT /api/shop/member/grade/{gradeId} - Update a member grade by ID")
    void updateMemberGrade() throws Exception {
        long gradeId = 1L;
        MemberGradeRequestDto requestDto = new MemberGradeRequestDto("플레", 300000, 10);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        mockMvc.perform(put("/api/shop/member/grade/{gradeId}",
                gradeId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("member-grade-update",
                pathParameters(
                    parameterWithName("gradeId").description("Grade ID")
                ),
                requestFields(
                    fieldWithPath("gradeName").type(JsonFieldType.STRING)
                        .description("New Grade Name")
                        .attributes(key("constraints").value("NotNull, length 1-15")),
                    fieldWithPath("gradeCondition").type(JsonFieldType.NUMBER)
                        .description("New Condition for Grade")
                        .attributes(key("constraints").value("NotNull")),
                    fieldWithPath("point").type(JsonFieldType.NUMBER)
                        .description("New Points associated with Grade")
                        .attributes(key("constraints").value("NotNull"))
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("Response Message")
                )));
    }

    @Test
    @DisplayName("DELETE /api/shop/member/grade/{gradeId} - Delete a member grade by ID")
    void deleteMemberGrade() throws Exception {
        long gradeId = 1L;

        mockMvc.perform(delete("/api/shop/member/grade/{gradeId}",
                gradeId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("member-grade-delete",
                pathParameters(
                    parameterWithName("gradeId").description("Grade ID")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("Response Message")
                )));
    }

    @Test
    @DisplayName("POST /api/shop/member/grade - Bad Request on Invalid Input")
    void createMemberGradeBadRequest() throws Exception {
        mockMvc.perform(post("/api/shop/member/grade")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{ \"gradeName\": \"\", \"gradeCondition\": -1, \"point\": 100 }")) // Invalid gradeName
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andDo(document("member-grade-create-bad-request",
                requestFields(
                    fieldWithPath("gradeName").type(JsonFieldType.STRING).description("Grade Name")
                        .attributes(key("constraints").value("NotNull, length 1-15")),
                    fieldWithPath("gradeCondition").type(JsonFieldType.NUMBER)
                        .description("Condition for Grade")
                        .attributes(key("constraints").value("NotNull")),
                    fieldWithPath("point").type(JsonFieldType.NUMBER)
                        .description("Points associated with Grade")
                        .attributes(key("constraints").value("NotNull"))
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("Response Message")
                )));
    }

    @Test
    @DisplayName("PUT /api/shop/member/grade/{gradeId} - Bad Request on Invalid Input")
    void updateMemberGradeBadRequest() throws Exception {
        mockMvc.perform(put("/api/shop/member/grade/{gradeId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{ \"gradeName\": \"\", \"gradeCondition\": -1, \"point\": 200 }")) // Invalid gradeName
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andDo(document("member-grade-update-bad-request",
                pathParameters(
                    parameterWithName("gradeId").description("Grade ID")
                ),
                requestFields(
                    fieldWithPath("gradeName").type(JsonFieldType.STRING)
                        .description("New Grade Name")
                        .attributes(key("constraints").value("NotNull, length 1-15")),
                    fieldWithPath("gradeCondition").type(JsonFieldType.NUMBER)
                        .description("New Condition for Grade")
                        .attributes(key("constraints").value("NotNull")),
                    fieldWithPath("point").type(JsonFieldType.NUMBER)
                        .description("New Points associated with Grade")
                        .attributes(key("constraints").value("NotNull"))
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("Response Message")
                )));
    }

    @Test
    @DisplayName("DELETE /api/shop/member/grade/{gradeId} - Bad Request on Invalid ID")
    void deleteMemberGradeBadRequest() throws Exception {
        mockMvc.perform(delete("/api/shop/member/grade/{gradeId}", 0) // Invalid gradeId
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andDo(document("member-grade-delete-bad-request",
                pathParameters(
                    parameterWithName("gradeId").description("Grade ID")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("Response Message")
                )));
    }
}
