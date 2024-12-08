package shop.s5g.shop.controller.refund;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.s5g.shop.test.domain.TestConstants.TEST_AUTHORIZATION;
import static shop.s5g.shop.test.utils.RestDocsHelper.SNIPPET_AUTH_HEADER;
import static shop.s5g.shop.test.utils.RestDocsHelper.customDocs;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import shop.s5g.shop.advice.AuthenticationAdvice;
import shop.s5g.shop.annotation.WithCustomMockUser;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.dto.refund.RefundHistoryCreateResponseDto;
import shop.s5g.shop.exception.refund.RefundConditionNotFulfilledException;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.order.RefundHistoryService;

@WebMvcTest(
    value = RefundController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import({TestSecurityConfig.class, AuthenticationAdvice.class})
@EnableAspectJAutoProxy
@AutoConfigureRestDocs
class RefundControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    RefundHistoryService refundHistoryService;

    String validRefund = """
        {
            "orderDetailId": 1,
            "typeId": 1,
            "reason": "아무튼 반품해 주세요",
            "images": [ "https://example-domain.com/image.png" ]
        }
        """;

    String invalidRefund = """
        {
            "orderDetailId": 0,
            "typeId": -1,
            "reason": null,
            "images": [ "https://example-domain.com/image.png" ]
        }
        """;
    @Test
    @WithCustomMockUser(loginId = "123", role = "ROLE_MEMBER", customerId = 1L)
    void createRefundTest() throws Exception {
        when(refundHistoryService.createNewRefund(anyLong(), any())).thenReturn(new RefundHistoryCreateResponseDto(1L));

        mvc.perform(MockMvcRequestBuilders.post("/api/shop/refund")
                .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRefund)
            )
            .andExpect(status().isCreated())
            .andDo(customDocs("refund-create", SNIPPET_AUTH_HEADER, requestFields(
                fieldWithPath("orderDetailId").description("반품할 주문 상세 고유 ID"),
                fieldWithPath("typeId").description("반품 타입 고유 ID"),
                fieldWithPath("reason").description("반품 사유"),
                fieldWithPath("images").description("반품 이미지 링크 리스트")
            ), responseFields(
                fieldWithPath("refundId").description("생성된 반품 고유 ID")
            )));

        verify(refundHistoryService, times(1)).createNewRefund(anyLong(), any());
    }

    @Test
    @WithCustomMockUser(loginId = "123", role = "ROLE_MEMBER", customerId = 1L)
    void createRefundValidationFailureTest() throws Exception {
        when(refundHistoryService.createNewRefund(anyLong(), any())).thenReturn(new RefundHistoryCreateResponseDto(1L));

        mvc.perform(MockMvcRequestBuilders.post("/api/shop/refund")
                .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRefund)
            )
            .andExpect(status().isBadRequest())
            .andDo(customDocs("refund-create-validation-failure", SNIPPET_AUTH_HEADER));

        verify(refundHistoryService, never()).createNewRefund(anyLong(), any());
    }

    @ParameterizedTest(name = "[{index}] id={1}")
    @MethodSource("shop.s5g.shop.controller.refund.RefundControllerTest#exceptionProvider")
    @WithCustomMockUser(loginId = "123", role = "ROLE_MEMBER", customerId = 1L)
    void createRefundConditionNotFulfilledTest(Exception e, String identifier) throws Exception{
        when(refundHistoryService.createNewRefund(anyLong(), any())).thenThrow(e);

        mvc.perform(MockMvcRequestBuilders.post("/api/shop/refund")
                .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRefund)
            )
            .andExpect(status().isBadRequest())
            .andDo(customDocs(identifier, SNIPPET_AUTH_HEADER));

        verify(refundHistoryService, times(1)).createNewRefund(anyLong(), any());
    }

    static Stream<? extends Arguments> exceptionProvider() {
        return Stream.of(
            Arguments.of(
                new RefundConditionNotFulfilledException("일반 반품은 10일 이내 미사용 시에만 반품 가능합니다."),
                "refund-create-condition-not-fulfilled-normal"
            ),
            Arguments.of(
                new RefundConditionNotFulfilledException("파손/파본에 의한 반품은 출고일 기준 30일 까지 가능합니다."),
                "refund-create-condition-not-fulfilled-special"
            ),
            Arguments.of(
                new RefundConditionNotFulfilledException("이미 확정된 주문입니다."),
                "refund-create-already-confirmed"
            )
        );
    }

}
