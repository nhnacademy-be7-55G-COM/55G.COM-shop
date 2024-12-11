package shop.s5g.shop.controller.customer;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.s5g.shop.test.utils.RestDocsHelper.customDocs;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import shop.s5g.shop.advice.AuthenticationAdvice;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.dto.customer.CustomerResponseDto;
import shop.s5g.shop.exception.member.CustomerNotFoundException;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.member.CustomerService;

@WebMvcTest(
    value = CustomerController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import({TestSecurityConfig.class, AuthenticationAdvice.class})
@EnableAspectJAutoProxy
@AutoConfigureRestDocs
class CustomerControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    CustomerService customerService;

    MultiValueMap<String, String> requestForm = new LinkedMultiValueMap<>();

    @BeforeEach
    void initRequestForm() {
        requestForm.add("type", "phoneNumber");
        requestForm.add("name", "testName");
        requestForm.add("password", "testPassword");
        requestForm.add("phoneNumber", "01012345678");
        requestForm.add("email", "test@example.org");
    }

    CustomerResponseDto response = new CustomerResponseDto(
        1L, DigestUtils.sha256Hex("testPassword"), "testName", "01029233456", null
    );

    @Test
    void getCustomerInfoTest() throws Exception{
        when(customerService.queryCustomer(anyString(), anyString(), anyString())).thenReturn(response);

        requestForm.remove("email");
        mvc.perform(MockMvcRequestBuilders.get("/api/shop/customer")
                .params(requestForm))
            .andExpect(status().isOk())
            .andDo(customDocs("customer-get-info", queryParameters(
                parameterWithName("type").description("고객을 찾는 방법. (email-현재 미지원-, phoneNumber)"),
                parameterWithName("email").optional().description("이메일 타입 시 필요한 파라미터. test@example.org"),
                parameterWithName("phoneNumber").optional().description("휴대폰 번호 타입 시 필요한 파라미터. [010\\d{8}]"),
                parameterWithName("name").description("고객 이름"),
                parameterWithName("password").description("고객의 비밀번호 (평문)")
            ), responseFields(
                fieldWithPath("customerId").description("고객의 고유 ID"),
                fieldWithPath("password").description("고객의 비밀번호 (암호화됨)"),
                fieldWithPath("name").description("고객의 이름"),
                fieldWithPath("phoneNumber").type("String/Null").optional().description("고객의 휴대폰 번호"),
                fieldWithPath("email").type("String/Null").optional().description("고객의 이메일")
            )));

        verify(customerService, times(1)).queryCustomer(anyString(), anyString(), anyString());
    }

    @Test
    void getCustomerInfoFailTest() throws Exception{
        requestForm.remove("email");
        requestForm.remove("type");;
        requestForm.add("type", "badType");
        mvc.perform(MockMvcRequestBuilders.get("/api/shop/customer")
                .params(requestForm))
            .andExpect(status().isBadRequest())
            .andDo(customDocs("customer-get-info-invalid-type"));

        verify(customerService, never()).queryCustomer(anyString(), anyString(), anyString());
    }

    @Test
    void getCustomerInfoNotFoundTest() throws Exception {
        when(customerService.queryCustomer(anyString(), anyString(), anyString())).thenThrow(new CustomerNotFoundException("휴대폰 번호와 비밀번호가 맞는 고객이 존재하지 않습니다."));

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/customer")
            .params(requestForm))
            .andExpect(status().isNotFound())
            .andDo(customDocs("customer-get-info-not-found"));

        verify(customerService, times(1)).queryCustomer(
            requestForm.getFirst("phoneNumber"), requestForm.getFirst("name"), requestForm.getFirst("password")
        );
    }

    String request = """
        {
            "password": "safasdf",
            "name": "sfadf",
            "phoneNumber": "01012345790",
            "email": null
        }
        """;

    @Test
    void createOrReturnCustomerForPurchaseTest() throws Exception{
        when(customerService.getOrRegisterCustomerPhoneNumber(anyString(), anyString(), anyString())).thenReturn(response);

        mvc.perform(MockMvcRequestBuilders.post("/api/shop/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .param("type", "phoneNumber"))
            .andExpect(status().isOk())
            .andDo(customDocs("customer-create-for-purchase", queryParameters(
                parameterWithName("type").description("고객을 등록하는 방법. (email-현재 미지원-, phoneNumber)")
            ), requestFields(
                fieldWithPath("name").description("고객의 이름. (길이 1이상 30이하)"),
                fieldWithPath("password").description("고객의 비밀번호 (평문, 최대 255자)"),
                fieldWithPath("phoneNumber").optional().description("고객의 휴대폰번호. (현재로썬 필수, 11자, reg=010\\d{8})"),
                fieldWithPath("email").optional().description("고객의 이메일, 현재 지원되지 않는 옵션, 비워놓기")
            ), responseFields(
                fieldWithPath("customerId").description("고객의 고유 ID"),
                fieldWithPath("password").description("고객의 비밀번호 (암호화됨)"),
                fieldWithPath("name").description("고객의 이름"),
                fieldWithPath("phoneNumber").type("String/Null").optional().description("고객의 휴대폰 번호"),
                fieldWithPath("email").type("String/Null").optional().description("고객의 이메일")
            )));

        verify(customerService, times(1)).getOrRegisterCustomerPhoneNumber(anyString(), anyString(), anyString());
    }

    @Test
    void createOrReturnCustomerForPurchaseWrongType() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/api/shop/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .param("type", "asfoiaoijf"))
            .andExpect(status().isBadRequest())
            .andDo(customDocs("customer-create-invalid-type"));

        verify(customerService, never()).getOrRegisterCustomerPhoneNumber(anyString(), anyString(), anyString());
    }
}
