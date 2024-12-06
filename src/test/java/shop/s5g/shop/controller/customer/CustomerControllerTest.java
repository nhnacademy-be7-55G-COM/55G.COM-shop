package shop.s5g.shop.controller.customer;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import shop.s5g.shop.dto.customer.CustomerResponseDto;
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
class CustomerControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    CustomerService customerService;

    CustomerResponseDto response = new CustomerResponseDto(
        1L, "sdfas", "asfasf", "dfadsfs", "sdfasf"
    );

    @Test
    void getCustomerInfoTest() throws Exception{
        when(customerService.queryCustomer(anyString(), anyString(), anyString())).thenReturn(response);

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/customer")
            .param("type", "phoneNumber")
            .param("name", "asdfasf")
            .param("password", "00128309")
            .param("phoneNumber", "12312490"))
            .andExpect(status().isOk());

        verify(customerService, times(1)).queryCustomer(anyString(), anyString(), anyString());
    }

    @Test
    void getCustomerInfoFailTest() throws Exception{
        mvc.perform(MockMvcRequestBuilders.get("/api/shop/customer")
                .param("type", "slajfdla;skjdf")
                .param("name", "asdfasf")
                .param("password", "Dafsdf")
                .param("phoneNumber", "12312490"))
            .andExpect(status().isBadRequest());

        verify(customerService, never()).queryCustomer(anyString(), anyString(), anyString());
    }

    String request = """
        {
            "password": "safasdf",
            "name": "sfadf",
            "phoneNumber": "01012345790",
            "email": "example@example.org"
        }
        """;

    @Test
    void createOrReturnCustomerForPurchaseTest() throws Exception{
        when(customerService.queryCustomer(anyString(), anyString(), anyString())).thenReturn(response);

        mvc.perform(MockMvcRequestBuilders.post("/api/shop/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .param("type", "phoneNumber"))
            .andExpect(status().isOk());

        verify(customerService, times(1)).getOrRegisterCustomerPhoneNumber(anyString(), anyString(), anyString());
    }

    @Test
    void createOrReturnCustomerForPurchaseWrongType() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/api/shop/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .param("type", "asfoiaoijf"))
            .andExpect(status().isBadRequest());

        verify(customerService, never()).getOrRegisterCustomerPhoneNumber(anyString(), anyString(), anyString());
    }
}
