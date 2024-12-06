package shop.s5g.shop.controller.delivery;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.s5g.shop.test.utils.RestDocsHelper.customDocs;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import shop.s5g.shop.advice.AuthenticationAdvice;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.controller.advice.RestWebAdvice;
import shop.s5g.shop.dto.delivery.DeliveryFeeResponseDto;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.delivery.DeliveryFeeService;

@WebMvcTest(
    value = DeliveryFeeController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import({TestSecurityConfig.class, AuthenticationAdvice.class})
@EnableAspectJAutoProxy
@AutoConfigureRestDocs
class DeliveryFeeControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    DeliveryFeeService deliveryFeeService;

    @SpyBean
    RestWebAdvice advice;

    String successUpdate = """
        {
            "id": 1,
            "fee": 3000,
            "condition": 0,
            "refundFee": 4000,
            "name": "배송비"
        }
        """;
    String failUpdate= """
        {
            "id": 1,
            "fee": -3000,
            "condition": 0,
            "refundFee": 4000,
            "name": "배송비"
        }
        """;

    DeliveryFeeResponseDto response = new DeliveryFeeResponseDto(
        1L, 3000L, 0, 3000, "배달"
    );

    @Test
    void updateDeliveryFeeFailTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/shop/delivery/fee")
            .contentType(MediaType.APPLICATION_JSON)
            .content(failUpdate)
        ).andExpect(status().isBadRequest())
            .andDo(customDocs("delivery-fee-update-fail"));

        verify(deliveryFeeService, never()).updateFee(any());
        verify(advice, times(1)).handleBadRequestException(any());
    }

    @Test
    void updateDeliveryFeeSuccessTest() throws Exception {
        when(deliveryFeeService.updateFee(any())).thenReturn(response);

        mvc.perform(MockMvcRequestBuilders.put("/api/shop/delivery/fee")
            .contentType(MediaType.APPLICATION_JSON)
            .content(successUpdate)
        ).andExpect(status().isOk())
            .andDo(customDocs("delivery-fee-update", requestFields(
                fieldWithPath("id").description("배송비 항목의 고유 식별자. 1 이상이어야 합니다."),
                fieldWithPath("fee").description("배송비 금액. 0 이상이어야 합니다."),
                fieldWithPath("condition").description("무료 배송을 위한 최소 주문 금액. 0 이상이어야 합니다."),
                fieldWithPath("refundFee").description("취소된 배송에 대한 환불 수수료. 0 이상이어야 합니다."),
                fieldWithPath("name").description("배송비 구성 이름. 길이는 1~20자 사이여야 합니다.")
            ), responseFields(
                fieldWithPath("id").description("배송비 항목의 고유 식별자"),
                fieldWithPath("fee").description("배송비 금액"),
                fieldWithPath("condition").description("무료 배송을 위한 최소 주문 금액"),
                fieldWithPath("refundFee").description("취소된 배송에 대한 환불 수수료"),
                fieldWithPath("name").description("배송비 구성 이름")
            )));

        verify(deliveryFeeService, times(1)).updateFee(any());
    }

    @Test
    void fetchDeliveryFees() throws Exception {
        when(deliveryFeeService.getAllDeliveryFees()).thenReturn(List.of(response));

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/delivery/fee"))
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].fee").value(3000))
            .andDo(customDocs("delivery-fee-fetch", responseFields(
                fieldWithPath("[].id").description("배송비 항목의 고유 식별자"),
                fieldWithPath("[].fee").description("배송비 금액"),
                fieldWithPath("[].condition").description("무료 배송을 위한 최소 주문 금액"),
                fieldWithPath("[].refundFee").description("취소된 배송에 대한 환불 수수료"),
                fieldWithPath("[].name").description("배송비 구성 이름")
            )));
    }

    String create = """
        {
            "fee": 5000,
            "condition": 0,
            "refundFee": 7000,
            "name": "두번째 배송"
        }
        """;
    String failCreate = """
        {
            "fee": -5000,
            "condition": 0,
            "refundFee": 7000,
            "name": "두번째 배송"
        }
        """;

    @Test
    void createDeliveryFeeFailTest() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/api/shop/delivery/fee")
            .contentType(MediaType.APPLICATION_JSON)
            .content(failCreate)
        ).andExpect(status().isBadRequest())
            .andDo(customDocs("delivery-fee-create-fail"));

        verify(advice, times(1)).handleBadRequestException(any());
        verify(deliveryFeeService, never()).createFee(any());
    }
    @Test
    void createDeliveryFeeSuccessTest() throws Exception{
        when(deliveryFeeService.createFee(any())).thenReturn(new DeliveryFeeResponseDto(
            1L, 5000L, 0L, 7000, "두번째 배송"
        ));

        mvc.perform(MockMvcRequestBuilders.post("/api/shop/delivery/fee")
            .contentType(MediaType.APPLICATION_JSON)
            .content(create)
        ).andExpect(status().isCreated())
            .andDo(customDocs("delivery-fee-create", requestFields(
                fieldWithPath("fee").description("배송비 금액. 0 이상이어야 합니다."),
                fieldWithPath("condition").description("무료 배송을 위한 최소 주문 금액. 0 이상이어야 합니다."),
                fieldWithPath("refundFee").description("취소된 배송에 대한 환불 수수료. 0 이상이어야 합니다."),
                fieldWithPath("name").description("배송비 구성 이름. 길이는 1~20자 사이여야 합니다.")
            ), responseFields(
                fieldWithPath("id").description("배송비 항목의 고유 식별자"),
                fieldWithPath("fee").description("배송비 금액"),
                fieldWithPath("condition").description("무료 배송을 위한 최소 주문 금액"),
                fieldWithPath("refundFee").description("취소된 배송에 대한 환불 수수료"),
                fieldWithPath("name").description("배송비 구성 이름")
            )));

        verify(deliveryFeeService, times(1)).createFee(any());
    }
}
