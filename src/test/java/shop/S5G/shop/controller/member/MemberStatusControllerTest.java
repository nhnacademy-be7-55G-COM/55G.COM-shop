package shop.S5G.shop.controller.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
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
import shop.S5G.shop.config.SecurityConfig;
import shop.S5G.shop.config.TestSecurityConfig;
import shop.S5G.shop.dto.memberStatus.MemberStatusRequestDto;
import shop.S5G.shop.dto.memberStatus.MemberStatusResponseDto;
import shop.S5G.shop.exception.BadRequestException;
import shop.S5G.shop.filter.JwtAuthenticationFilter;
import shop.S5G.shop.service.member.MemberStatusService;

@AutoConfigureRestDocs
@WebMvcTest(
    value = MemberStatusController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import(TestSecurityConfig.class)
class MemberStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberStatusService memberStatusService;

    @Test
    @DisplayName("GET /api/shop/member/status - Get all member statuses")
    void getMemberStatus() throws Exception {
        List<MemberStatusResponseDto> memberStatusList = List.of(
            new MemberStatusResponseDto(1L, "Active"),
            new MemberStatusResponseDto(2L, "Inactive")
        );

        when(memberStatusService.getAllMemberStatus()).thenReturn(memberStatusList);

        mockMvc.perform(get("/api/shop/member/status")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("member-status-list",
                responseFields(
                    fieldWithPath("[].memberStatusId").type(JsonFieldType.NUMBER)
                        .description("Member Status ID"),
                    fieldWithPath("[].typeName").type(JsonFieldType.STRING).description("Type Name")
                )));
    }

    @Test
    @DisplayName("GET /api/shop/member/status/{memberStatusId} - Get member status by ID")
    void getMemberStatusById() throws Exception {
        MemberStatusResponseDto memberStatusResponseDto = new MemberStatusResponseDto(1L, "Active");

        when(memberStatusService.getMemberStatus(1L)).thenReturn(memberStatusResponseDto);

        mockMvc.perform(get("/api/shop/member/status/{memberStatusId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("member-status-get",
                pathParameters(
                    parameterWithName("memberStatusId").description("Member Status ID")
                ),
                responseFields(
                    fieldWithPath("memberStatusId").type(JsonFieldType.NUMBER)
                        .description("Member Status ID"),
                    fieldWithPath("typeName").type(JsonFieldType.STRING).description("Type Name")
                )));
    }

    @Test
    @DisplayName("POST /api/shop/member/status - Create a new member status")
    void createMemberStatus() throws Exception {

        mockMvc.perform(post("/api/shop/member/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"typeName\": \"Active\"}"))
            .andExpect(status().isCreated())
            .andDo(print())
            .andDo(document("member-status-create",
                requestFields(
                    fieldWithPath("typeName").type(JsonFieldType.STRING).description("Type Name")
                        .attributes(key("constraints").value("NotNull, NotEmpty, length 0-15"))
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("Response Message")
                )));
    }

    @Test
    @DisplayName("PUT /api/shop/member/status/{memberStatusId} - Update a member status by ID")
    void updateMemberStatus() throws Exception {

        mockMvc.perform(put("/api/shop/member/status/{memberStatusId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"typeName\": \"Inactive\"}"))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("member-status-update",
                pathParameters(
                    parameterWithName("memberStatusId").description("Member Status ID")
                ),
                requestFields(
                    fieldWithPath("typeName").type(JsonFieldType.STRING)
                        .description("New Type Name")
                        .attributes(key("constraints").value("NotNull, NotEmpty, length 0-15"))
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("Response Message")
                )));
    }

    @Test
    @DisplayName("PUT /api/shop/member/status/{memberStatusId} - Update member status with invalid request")
    void updateMemberStatusWithInvalidRequest() throws Exception {
        Long memberStatusId = 1L;

        doThrow(new BadRequestException("잘못된 요청입니다")).when(memberStatusService)
            .updateMemberStatus(anyLong(), any(
                MemberStatusRequestDto.class));

        mockMvc.perform(put("/api/shop/member/status/{memberStatusId}", memberStatusId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"typeName\": \"\"}"))
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andDo(document("member-status-update-invalid",
                pathParameters(
                    parameterWithName("memberStatusId").description("Member Status ID")
                ),
                requestFields(
                    fieldWithPath("typeName").type(JsonFieldType.STRING).description("Type Name")
                        .attributes(key("constraints").value("NotNull, NotEmpty, length 0-15"))
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("Error Message")
                )));
    }

    @Test
    @DisplayName("POST /api/shop/member/status - Create member status with invalid request")
    void createMemberStatusWithInvalidRequest() throws Exception {

        doThrow(new BadRequestException("잘못된 요청입니다")).when(memberStatusService)
            .saveMemberStatus(any(MemberStatusRequestDto.class));

        mockMvc.perform(post("/api/shop/member/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"typeName\": \"\"}"))
            .andExpect(status().isBadRequest())
            .andDo(print())
            .andDo(document("member-status-create-invalid",
                requestFields(
                    fieldWithPath("typeName").type(JsonFieldType.STRING).description("Type Name")
                        .attributes(key("constraints").value("NotNull, NotEmpty, length 0-15"))
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING).description("Error Message")
                )));
    }
}
