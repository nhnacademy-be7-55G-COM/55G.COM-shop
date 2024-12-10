package shop.s5g.shop.controller.member;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.dto.address.AddressRequestDto;
import shop.s5g.shop.dto.address.AddressResponseDto;
import shop.s5g.shop.dto.address.AddressUpdateRequestDto;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.security.ShopMemberDetail;
import shop.s5g.shop.service.member.AddressService;

@AutoConfigureRestDocs
@WebMvcTest(
    value = AddressController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import(TestSecurityConfig.class)
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    @Test
    void getAddresses_shouldReturnAddressList() throws Exception {
        // Given
        ShopMemberDetail memberDetail = new ShopMemberDetail("testUser", "ROLE_USER", 1L);
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(memberDetail, null,
                memberDetail.getAuthorities())
        );

        List<AddressResponseDto> addressList = List.of(
            new AddressResponseDto(1L, "123 Main St", "Apt 4B", "Home", true),
            new AddressResponseDto(2L, "456 Elm St", "Suite 1A", "Office", false)
        );

        when(addressService.getAddresses(1L)).thenReturn(addressList);

        // When & Then
        mockMvc.perform(get("/api/shop/address"))
            .andExpect(status().isOk())
            .andDo(print())
            .andDo(document("get-addresses",
                responseFields(
                    fieldWithPath("[].addressId").type(JsonFieldType.NUMBER)
                        .description("Address ID"),
                    fieldWithPath("[].primaryAddress").type(JsonFieldType.STRING)
                        .description("Primary address"),
                    fieldWithPath("[].detailAddress").type(JsonFieldType.STRING)
                        .description("Detailed address"),
                    fieldWithPath("[].alias").type(JsonFieldType.STRING)
                        .description("Address alias"),
                    fieldWithPath("[].isDefault").type(JsonFieldType.BOOLEAN)
                        .description("Is default address")
                )));

        verify(addressService).getAddresses(1L);
    }

    @Test
    void createAddress_shouldSaveAddress() throws Exception {
        // Given
        ShopMemberDetail memberDetail = new ShopMemberDetail("testUser", "ROLE_USER", 1L);
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(memberDetail, null,
                memberDetail.getAuthorities())
        );

        AddressRequestDto requestDto = new AddressRequestDto("123 Main St", "Apt 4B", "Home", true);

        // When & Then
        mockMvc.perform(post("/api/shop/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "primaryAddress": "123 Main St",
                        "detailAddress": "Apt 4B",
                        "alias": "Home",
                        "isDefault": true
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("주소 저장 성공"))
            .andDo(print())
            .andDo(document("create-address",
                requestFields(
                    fieldWithPath("primaryAddress").type(JsonFieldType.STRING)
                        .description("Primary address"),
                    fieldWithPath("detailAddress").type(JsonFieldType.STRING)
                        .description("Detailed address"),
                    fieldWithPath("alias").type(JsonFieldType.STRING).description("Address alias"),
                    fieldWithPath("isDefault").type(JsonFieldType.BOOLEAN)
                        .description("Is default address")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("Response message")
                )));

        verify(addressService).save(any(AddressRequestDto.class), eq(1L));
    }

    @Test
    void deleteAddress_shouldDeleteAddress() throws Exception {
        // Given
        Long addressId = 1L;

        // When & Then
        mockMvc.perform(delete("/api/shop/address/{addressId}", addressId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("삭제 성공"))
            .andDo(print())
            .andDo(document("delete-address",
                pathParameters(
                    parameterWithName("addressId").description("ID of the address to delete")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("Response message")
                )));

        verify(addressService).delete(addressId);
    }

    @Test
    void updateAddress_shouldUpdateAddress() throws Exception {
        // Given
        Long addressId = 1L;

        // When & Then
        mockMvc.perform(put("/api/shop/address/{addressId}", addressId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "alias": "Updated Home",
                        "isDefault": false
                    }
                    """))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message").value("변경 성공"))
            .andDo(print())
            .andDo(document("update-address",
                pathParameters(
                    parameterWithName("addressId").description("ID of the address to update")
                ),
                requestFields(
                    fieldWithPath("alias").type(JsonFieldType.STRING)
                        .description("Updated address alias"),
                    fieldWithPath("isDefault").type(JsonFieldType.BOOLEAN)
                        .description("Is default address")
                ),
                responseFields(
                    fieldWithPath("message").type(JsonFieldType.STRING)
                        .description("Response message")
                )));

        verify(addressService).update(any(AddressUpdateRequestDto.class), eq(addressId));
    }
}