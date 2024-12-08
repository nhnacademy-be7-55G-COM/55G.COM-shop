package shop.s5g.shop.controller.order;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.s5g.shop.test.domain.TestConstants.TEST_AUTHORIZATION;
import static shop.s5g.shop.test.domain.TestConstants.TEST_UUID;
import static shop.s5g.shop.test.utils.RestDocsHelper.SNIPPET_AUTH_HEADER;
import static shop.s5g.shop.test.utils.RestDocsHelper.customDocs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import shop.s5g.shop.advice.AuthenticationAdvice;
import shop.s5g.shop.annotation.WithCustomMockUser;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.controller.advice.RestWebAdvice;
import shop.s5g.shop.dto.customer.CustomerResponseDto;
import shop.s5g.shop.dto.order.OrderAdminTableView;
import shop.s5g.shop.dto.order.OrderCreateResponseDto;
import shop.s5g.shop.dto.order.OrderWithDetailResponseDto;
import shop.s5g.shop.entity.delivery.DeliveryStatus;
import shop.s5g.shop.entity.delivery.DeliveryStatus.Type;
import shop.s5g.shop.exception.ResourceNotFoundException;
import shop.s5g.shop.exception.member.CustomerNotFoundException;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.member.CustomerService;
import shop.s5g.shop.service.order.OrderService;

@WebMvcTest(
    value = OrderController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import({TestSecurityConfig.class, AuthenticationAdvice.class})
@EnableAspectJAutoProxy
@AutoConfigureRestDocs
class OrderControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    OrderService orderService;

    @MockBean
    CustomerService customerService;

    @SpyBean
    RestWebAdvice advice;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Test
    @WithCustomMockUser(loginId = "123", customerId = 1L, role = "ROLE_MEMBER")
    void fetchOrdersQueryTest() throws Exception{
        when(orderService.getAllOrdersBetweenDates(anyLong(), any())).thenReturn(List.of(
            new OrderWithDetailResponseDto(1L, LocalDateTime.now().minusDays(10), 12000L, 12000L,
                "스프링 입문 등 3권", 2L, 3, TEST_UUID
            )
        ));

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/orders")
                .header("Authorization", TEST_AUTHORIZATION)
                .queryParam("startDate", LocalDate.now().minusMonths(1).format(formatter))
                .queryParam("endDate", LocalDate.now().format(formatter))
            )
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].totalPrice").value(12000L))
            .andDo(print())
            .andDo(customDocs("order-fetch", SNIPPET_AUTH_HEADER, queryParameters(
                parameterWithName("startDate").description("검색 시작 날짜. (yyyy-MM-dd)"),
                parameterWithName("endDate").description("검색 종료 날짜. (yyyy-MM-dd)")
            ), responseFields(
                fieldWithPath("[].orderId").description("주문의 고유 식별자"),
                fieldWithPath("[].orderedAt").description("주문이 생성된 날짜 및 시간 (예: 2024-12-06T12:34:56)"),
                fieldWithPath("[].netPrice").description("주문 상품의 순수 가격 (할인 적용 전 금액)"),
                fieldWithPath("[].totalPrice").description("주문의 총 금액 (할인 및 배송비 포함)"),
                fieldWithPath("[].representTitle").description("리스트에서 보여질 대표 상품 정보 (예: '예시타이틀' 2권 외...)"),
                fieldWithPath("[].totalKind").description("주문된 상품 종류 수 (예: 총 N종)"),
                fieldWithPath("[].totalQuantity").description("주문된 상품의 총 수량 (예: 총 N권)"),
                fieldWithPath("[].uuid").description("주문에 대한 UUID (고유 식별 문자열)")
            )));
        verify(orderService, times(1)).getAllOrdersBetweenDates(anyLong(), any());
    }

    @Test
    @WithCustomMockUser(loginId = "123", customerId = 1L, role = "ROLE_MEMBER")
    void fetchOrdersTest() throws Exception {
        OrderWithDetailResponseDto dto = new OrderWithDetailResponseDto(
            1L, LocalDateTime.now(), 3000L, 5000L, "test title", 3, 4, TEST_UUID
        );
        List<OrderWithDetailResponseDto> result =  List.of(dto, dto);

        when(orderService.getAllOrdersWithDetail(anyLong())).thenReturn(result);

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/orders")
                .header("Authorization", TEST_AUTHORIZATION))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("\"representTitle\":\"test title\"")))
            .andDo(print());

        verify(orderService, times(1)).getAllOrdersWithDetail(1L);
    }

    String validatedTestCase = """
        {
            "delivery": {
                "address": "테스트 주소",
                "deliveryFeeId": 1,
                "receivedDate": "2024-10-30"
            },
            "cartList": [
                {
                    "bookId": 1,
                    "wrappingPaperId": 1,
                    "quantity": 1,
                    "totalPrice": 10000,
                    "accumulationPrice": 200
                }
            ],
            "netPrice": 99800,
            "totalPrice": 10000
        }
        """;
    // 비어있는 장바구니
    String failedTestCase = """
        {
            "delivery": {
                "address": "테스트 주소",
                "deliveryFeeId": 1,
                "receivedDate": "2024-10-30"
            },
            "cartList": [],
            "netPrice": 99800,
            "totalPrice": 10000
        }
        """;
    @Test
    @WithCustomMockUser(loginId = "123", customerId = 1L, role = "ROLE_MEMBER")
    void createNewOrderValidateFailTest() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/api/shop/orders")
                .header("Authorization", TEST_AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(failedTestCase)
            ).andExpect(status().isBadRequest())
            .andDo(customDocs("order-create-validation-fail", SNIPPET_AUTH_HEADER));
        verify(orderService, never()).createOrder(eq(1L), any());
    }

    @Test
    @WithCustomMockUser(loginId = "123", customerId = 1L, role = "ROLE_MEMBER")
    void createNewOrderSuccessTest() throws Exception{
        OrderCreateResponseDto response = new OrderCreateResponseDto(1L, "UUID");
        when(orderService.createOrder(anyLong(), any())).thenReturn(response);

        mvc.perform(MockMvcRequestBuilders.post("/api/shop/orders")
            .header("Authorization", TEST_AUTHORIZATION)
            .contentType(MediaType.APPLICATION_JSON)
            .content(validatedTestCase)
        )
            .andExpect(status().isCreated())
            .andExpect(content().string(containsString("\"orderId\":1")))
            .andDo(customDocs("order-create", SNIPPET_AUTH_HEADER, requestFields(
                fieldWithPath("delivery").description("배송 정보 객체"),
                fieldWithPath("delivery.address").description("배송지 주소"),
                fieldWithPath("delivery.deliveryFeeId").description("적용된 배송비 ID"),
                fieldWithPath("delivery.receivedDate").description("수령 희망 날짜 (yyyy-MM-dd 형식)"),
                fieldWithPath("cartList").description("장바구니 목록 배열"),
                fieldWithPath("cartList[].bookId").description("상품(책)의 고유 ID"),
                fieldWithPath("cartList[].wrappingPaperId").description("선택한 포장지의 고유 ID"),
                fieldWithPath("cartList[].quantity").description("주문 수량"),
                fieldWithPath("cartList[].totalPrice").description("해당 상품의 총 가격"),
                fieldWithPath("cartList[].accumulationPrice").description("해당 상품에 적립된 포인트"),
                fieldWithPath("netPrice").description("주문 상품의 총 가격 (할인 적용 전)"),
                fieldWithPath("totalPrice").description("주문의 총 결제 금액")
            ), responseFields(
                fieldWithPath("orderId").description("생성된 주문의 고유 ID"),
                fieldWithPath("uuid").description("생성된 주문의 고유 UUID")
            )))
        ;

        verify(orderService, times(1)).createOrder(eq(1L), any());
    }

    @Test
    @WithCustomMockUser(loginId = "123", customerId = 1L, role = "ROLE_MEMBER")
    void createNewOrderExceptionTest() throws Exception {
        when(orderService.createOrder(anyLong(), any())).thenThrow(new CustomerNotFoundException("Customer not found given id: 1"));

        mvc.perform(MockMvcRequestBuilders.post("/api/shop/orders")
                .header("Authorization", TEST_AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validatedTestCase)
            )
            .andExpect(status().isNotFound())
            .andDo(customDocs("order-create-invalid-customer", SNIPPET_AUTH_HEADER))
        ;

        verify(orderService, times(1)).createOrder(eq(1L), any());
    }

    @Test
    @WithCustomMockUser(loginId = "123", customerId = 1L, role = "ROLE_MEMBER")
    void fetchOrdersBetweenDatesTest() throws Exception{
        OrderWithDetailResponseDto dto = new OrderWithDetailResponseDto(
            1L, LocalDateTime.now(), 3000L, 5000L, "test title", 3, 4, TEST_UUID
        );
        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now();

        when(orderService.getAllOrdersBetweenDates(anyLong(), any())).thenReturn(List.of(dto));

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/orders")
            .header("Authorization", TEST_AUTHORIZATION)
            .queryParam("startDate", startDate.format(formatter))
            .queryParam("endDate", endDate.format(formatter))
        ).andExpect(jsonPath("$[0].representTitle").value("test title"));
    }

    @Test
    void deleteOrderFailTest() throws Exception {
        doThrow(ResourceNotFoundException.class).when(orderService).deactivateOrder(anyLong());
        mvc.perform(MockMvcRequestBuilders.delete("/api/shop/orders/1"))
            .andExpect(status().isNotFound())
            .andDo(customDocs("order-delete-not-found"));

        verify(orderService, times(1)).deactivateOrder(1L);
        verify(advice, times(1)).handleResourceNotFoundException(any());
    }
    @Test
    void deleteOrderSuccessTest() throws Exception {
        mvc.perform(RestDocumentationRequestBuilders.delete("/api/shop/orders/{orderId}", 1))
            .andExpect(status().isNoContent())
            .andDo(customDocs("order-delete", pathParameters(
                parameterWithName("orderId").description("삭제할 주문의 고유 ID")
            )));

        verify(orderService, times(1)).deactivateOrder(1L);
        verify(advice, never()).handleResourceNotFoundException(any());
    }

    @Test
    void unauthorizedTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/shop/orders"))
            .andExpect(status().isForbidden())
            .andDo(customDocs("order-fetch-unauthorized"));
        verify(advice, times(1)).handleForbiddenResourceException(any());
        verify(orderService, never()).getAllOrdersWithDetail(anyLong());
    }

    CustomerResponseDto customerResponse = new CustomerResponseDto(
        1L, "safdsaf", "name", "pn", "email@email"
    );

    @Test
    void queryAllGuestOrdersTest() throws Exception{
        OrderWithDetailResponseDto dto = new OrderWithDetailResponseDto(
            1L, LocalDateTime.now(), 3000L, 5000L, "test title", 3, 4, "79131bc8-7341-4994-bfb3-8cd15534023a"
        );
        when(customerService.queryCustomer(anyString(), anyString(), anyString())).thenReturn(customerResponse);
        when(orderService.getAllOrdersWithDetail(1L)).thenReturn(List.of(dto));

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/orders/guests")
            .param("phoneNumber", "01023818421")
            .param("name", "amugae")
            .param("password", "asfasdfasdf"))
            .andExpect(status().isOk())
            .andDo(customDocs("order-guest-query", queryParameters(
                parameterWithName("phoneNumber").description("게스트 사용자의 휴대폰 번호"),
                parameterWithName("name").description("게스트 사용자의 이름"),
                parameterWithName("password").description("게스트 사용자의 비밀번호(평문)")
            ), responseFields(
                fieldWithPath("[].orderId").description("주문의 고유 식별자"),
                fieldWithPath("[].orderedAt").description("주문이 생성된 날짜 및 시간 (예: 2024-12-06T12:34:56)"),
                fieldWithPath("[].netPrice").description("주문 상품의 순수 가격 (할인 적용 전 금액)"),
                fieldWithPath("[].totalPrice").description("주문의 총 금액 (할인 및 배송비 포함)"),
                fieldWithPath("[].representTitle").description("리스트에서 보여질 대표 상품 정보 (예: '예시타이틀' 2권 외...)"),
                fieldWithPath("[].totalKind").description("주문된 상품 종류 수 (예: 총 N종)"),
                fieldWithPath("[].totalQuantity").description("주문된 상품의 총 수량 (예: 총 N권)"),
                fieldWithPath("[].uuid").description("주문에 대한 UUID (고유 식별 문자열)")
            )));

        verify(orderService, times(1)).getAllOrdersWithDetail(1L);
        verify(customerService, times(1)).queryCustomer(anyString(), anyString(), anyString());
    }

    @Test
    void fetchOrdersForAdminTest() throws Exception{
        OrderAdminTableView testView = new OrderAdminTableView(
            1L, 23000L, 23000L,
            Type.SHIPPING.name(), LocalDateTime.now(), true, TEST_UUID
        );
        when(orderService.getOrderListAdmin(any())).thenReturn(List.of(testView));

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/orders/admin")
                .param("customerId", "1")
                .param("active", "true")
                .param("page", "1")
                .param("size", "10")
                .param("deliveryStatus", "SHIPPING")
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].totalPrice").value(23000L))
            .andDo(customDocs("order-fetch-admin", queryParameters(
                parameterWithName("orderId").optional().description("주문의 고유 ID (필수가 아니며, 1 이상의 값만 허용됩니다)"),
                parameterWithName("customerId").optional().description("고객의 고유 ID (필수가 아니며, 1 이상의 값만 허용됩니다)"),
                parameterWithName("active").optional().description("주문 활성화 여부를 나타내는 플래그 (true/false 값)"),
                parameterWithName("page").optional().description("요청 페이지 번호 (1 이상의 값만 허용됩니다)"),
                parameterWithName("size").optional().description("페이지당 항목 수 (1 이상의 값만 허용됩니다)"),
                parameterWithName("deliveryStatus").optional().description("배송 상태 필터링을 위한 값 (예: 'DELIVERED', 'PENDING')")
            ), responseFields(
                fieldWithPath("[].orderId").description("주문의 고유 ID"),
                fieldWithPath("[].totalPrice").description("주문의 총 금액"),
                fieldWithPath("[].payAmount").type("String/Null").optional().description("결제된 금액 (실제로 결제가 진행되지 않았다면 null.)"),
                fieldWithPath("[].deliveryStatus").description("배송 상태: " + Arrays.toString(DeliveryStatus.Type.values())),
                fieldWithPath("[].orderedAt").description("주문이 생성된 날짜 및 시간 (ISO-8601 형식, 예: '2024-12-06T12:34:56')"),
                fieldWithPath("[].active").description("주문 활성화 여부를 나타내는 플래그 (true/false)"),
                fieldWithPath("[].uuid").description("주문에 대한 UUID (고유 식별 문자열)")
            )));

        verify(orderService, times(1)).getOrderListAdmin(any());
    }

    @Test
    void fetchOrdersForAdminValidationTest() throws Exception {
        when(orderService.getOrderListAdmin(any())).thenReturn(List.of());

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/orders/admin")
                .param("customerId", "0"))
            .andExpect(status().isBadRequest())
            .andDo(customDocs("order-fetch-admin-validation-fail"));

        verify(orderService, never()).getOrderListAdmin(any());
    }

    @Test
    void createNewGuestOrderTest() throws Exception{
        OrderCreateResponseDto response = new OrderCreateResponseDto(1L, TEST_UUID);
        when(orderService.createOrder(anyLong(), any())).thenReturn(response);

        mvc.perform(RestDocumentationRequestBuilders.post("/api/shop/orders/guests/{customerId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validatedTestCase))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.orderId").value(1L))
            .andDo(customDocs("order-guest-create", pathParameters(
                parameterWithName("customerId").description("주문을 새로 만드는 게스트의 고유 ID")
            ), requestFields(
                fieldWithPath("delivery").description("배송 정보 객체"),
                fieldWithPath("delivery.address").description("배송지 주소"),
                fieldWithPath("delivery.deliveryFeeId").description("적용된 배송비 ID"),
                fieldWithPath("delivery.receivedDate").description("수령 희망 날짜 (yyyy-MM-dd 형식)"),
                fieldWithPath("cartList").description("장바구니 목록 배열"),
                fieldWithPath("cartList[].bookId").description("상품(책)의 고유 ID"),
                fieldWithPath("cartList[].wrappingPaperId").description("선택한 포장지의 고유 ID"),
                fieldWithPath("cartList[].quantity").description("주문 수량"),
                fieldWithPath("cartList[].totalPrice").description("해당 상품의 총 가격"),
                fieldWithPath("cartList[].accumulationPrice").description("해당 상품에 적립된 포인트"),
                fieldWithPath("netPrice").description("주문 상품의 총 가격 (할인 적용 전)"),
                fieldWithPath("totalPrice").description("주문의 총 결제 금액")
            ), responseFields(
                fieldWithPath("orderId").description("생성된 주문의 고유 ID"),
                fieldWithPath("uuid").description("생성된 주문의 고유 UUID")
            )));

        verify (orderService, times(1)).createOrder(anyLong(), any());
    }

    @Test
    void createNewOrderGuestExceptionTest() throws Exception {
        when(orderService.createOrder(anyLong(), any())).thenThrow(new CustomerNotFoundException("Customer not found given id: 1"));

        mvc.perform(MockMvcRequestBuilders.post("/api/shop/orders/guests/{customerId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(validatedTestCase)
            )
            .andExpect(status().isNotFound())
            .andDo(customDocs("order-guest-create-invalid-customer"))
        ;

        verify(orderService, times(1)).createOrder(eq(1L), any());
    }

    @Test
    void createNewGuestOrderValidateFailTest() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/api/shop/orders/guests/{customerId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(failedTestCase)
            ).andExpect(status().isBadRequest())
            .andDo(customDocs("order-guest-create-validation-fail"));
        verify(orderService, never()).createOrder(eq(1L), any());
    }
}
