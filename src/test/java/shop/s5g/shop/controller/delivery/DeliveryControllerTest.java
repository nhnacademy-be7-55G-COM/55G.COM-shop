package shop.s5g.shop.controller.delivery;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.s5g.shop.test.utils.RestDocsHelper.customDocs;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import shop.s5g.shop.advice.AuthenticationAdvice;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.dto.delivery.DeliveryResponseDto;
import shop.s5g.shop.dto.delivery.DeliveryUpdateRequestDto;
import shop.s5g.shop.entity.delivery.DeliveryStatus;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.delivery.DeliveryService;

@WebMvcTest(
    value = DeliveryController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import({TestSecurityConfig.class, AuthenticationAdvice.class})
@EnableAspectJAutoProxy
@AutoConfigureRestDocs
class DeliveryControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    DeliveryService deliveryService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    DeliveryUpdateRequestDto update = new DeliveryUpdateRequestDto(
        1L, "주소", null, LocalDate.now().plusDays(1)
    , null, "아무나", "PREPARING");

    String updateRequest = """
        {
            "id": 1,
            "address": "주소",
            "invoiceNumber": null,
            "receivedDate": "2090-10-30",
            "shippingDate": null,
            "receiverName": "아무나",
            "status": "PREPARING"
        }
        """;
    String failRequest = """
        {
            "id": 1,
            "address": "주소",
            "invoiceNumber": null,
            "receivedDate": "1999-10-30",
            "shippingDate": null,
            "receiverName": "아무나",
            "status": "PREPARING"
        }
        """;

    DeliveryResponseDto delivery = new DeliveryResponseDto(
        1L, "주소", LocalDate.of(2090, 10, 30), null, 3000L, null, "아무나", "PREPARING"
    );

    @Test
    void adminUpdateDeliveryTest() throws Exception{
        when(deliveryService.adminUpdateDelivery(any())).thenReturn(delivery);

        mvc.perform(MockMvcRequestBuilders.put("/api/shop/delivery/admin")
            .content(updateRequest)
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk())
            .andDo(customDocs("delivery-update-admin", requestFields(
                fieldWithPath("id").description("주문 ID (1 이상의 값이어야 합니다)"),
                fieldWithPath("address").description("배송지 주소 (1~100자 사이)"),
                fieldWithPath("invoiceNumber").type(JsonFieldType.STRING).optional().description("송장 번호 (최대 20자)"),
                fieldWithPath("receivedDate").description("수령 날짜 (yyyy-MM-dd 형식, 현재 날짜 또는 미래 날짜)"),
                fieldWithPath("shippingDate").type(JsonFieldType.STRING).optional().description("배송 날짜 (yyyy-MM-dd 형식, 선택 사항)"),
                fieldWithPath("receiverName").description("수령인 이름 (1~30자 사이)"),
                fieldWithPath("status").description("배송 상태 (필수)")
            ), responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("해당 주문에 대응하는 배송 ID"),
                fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
                fieldWithPath("receivedDate").type(JsonFieldType.STRING).description("받기를 원하는 날짜"),
                fieldWithPath("shippingDate").type("String/Null").description("출고일"),
                fieldWithPath("fee").type(JsonFieldType.NUMBER).description("배송비용"),
                fieldWithPath("invoiceNumber").type("String/Null").description("송장번호"),
                fieldWithPath("receiverName").type(JsonFieldType.STRING).description("수신자 성명"),
                fieldWithPath("status").type(JsonFieldType.STRING).description(Arrays.toString(DeliveryStatus.Type.values()))
            )));

        verify(deliveryService, times(1)).adminUpdateDelivery(any());
    }

    @Test
    void adminUpdateDeliveryFailTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/shop/delivery/admin")
                .content(failRequest)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andDo(customDocs("delivery-update-admin-fail"));

        verify(deliveryService, never()).adminUpdateDelivery(any());
    }

    @Test
    void userUpdateDeliveryFailTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/shop/delivery")
            .content(failRequest)
            .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest())
            .andDo(customDocs("delivery-update-user-fail"));

        verify(deliveryService, never()).userUpdateDelivery(any());
    }

    @Test
    void userUpdateDeliveryTest() throws Exception {
        when(deliveryService.userUpdateDelivery(any())).thenReturn(new DeliveryResponseDto(
            1L, "주소", LocalDate.of(2090, 10, 30), null, 5000L, null, "아무나", "PREPARING"
        ));

        mvc.perform(MockMvcRequestBuilders.put("/api/shop/delivery")
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateRequest)
        )
            .andExpect(status().isOk())
            .andDo(customDocs("delivery-update-user", requestFields(
                fieldWithPath("id").description("주문 ID (1 이상의 값이어야 합니다)"),
                fieldWithPath("address").description("배송지 주소 (1~100자 사이)"),
                fieldWithPath("invoiceNumber").type("String/Null").optional().description("송장 번호 (최대 20자)"),
                fieldWithPath("receivedDate").description("수령 날짜 (yyyy-MM-dd 형식, 현재 날짜 또는 미래 날짜)"),
                fieldWithPath("shippingDate").type("String/Null").optional().description("배송 날짜 (yyyy-MM-dd 형식, 선택 사항)"),
                fieldWithPath("receiverName").description("수령인 이름 (1~30자 사이)"),
                fieldWithPath("status").description("배송 상태 (필수)")
            ), responseFields(
                fieldWithPath("id").type(JsonFieldType.NUMBER).description("해당 주문에 대응하는 배송 ID"),
                fieldWithPath("address").type(JsonFieldType.STRING).description("주소"),
                fieldWithPath("receivedDate").type(JsonFieldType.STRING).description("받기를 원하는 날짜"),
                fieldWithPath("shippingDate").type("String/Null").description("출고일"),
                fieldWithPath("fee").type(JsonFieldType.NUMBER).description("배송비용"),
                fieldWithPath("invoiceNumber").type("String/Null").description("송장번호"),
                fieldWithPath("receiverName").type(JsonFieldType.STRING).description("수신자 성명"),
                fieldWithPath("status").type(JsonFieldType.STRING).description(Arrays.toString(DeliveryStatus.Type.values()))
            )));

        verify(deliveryService, times(1)).userUpdateDelivery(any());
    }

}
