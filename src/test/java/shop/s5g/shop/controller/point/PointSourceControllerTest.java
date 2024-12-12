package shop.s5g.shop.controller.point;


import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.dto.point.PointPolicyFormResponseDto;
import shop.s5g.shop.dto.point.PointSourceCreateRequestDto;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.point.PointSourceService;

@WebMvcTest(
    value = PointSourceController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import(TestSecurityConfig.class)
public class PointSourceControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    PointSourceService pointSourceService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getPointPolicyFormDataTest() throws Exception {
        List<PointPolicyFormResponseDto> responseList = new ArrayList<>();
        responseList.add(new PointPolicyFormResponseDto(1l, "testName1"));
        responseList.add(new PointPolicyFormResponseDto(2l, "testName2"));

        when(pointSourceService.getPointPolicyFormData()).thenReturn(responseList);

        mvc.perform(get("/api/shop/point/source/create"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].pointSourceId").value(1l))
            .andExpect(jsonPath("$[0].pointSourceName").value("testName1"))
            .andExpect(jsonPath("$[1].pointSourceId").value(2l))
            .andExpect(jsonPath("$[1].pointSourceName").value("testName2"));

    }

    @Test
    void createPointSourceTest() throws Exception {
        PointSourceCreateRequestDto pointSourceCreateRequestDto = new PointSourceCreateRequestDto(
            "testName");
        String requestBody = objectMapper.writeValueAsString(pointSourceCreateRequestDto);

        doNothing().when(pointSourceService).createPointSource(pointSourceCreateRequestDto);

        mvc.perform(post("/api/shop/point/source/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk());
    }

    @Test
    void createPointSourceValidationFailTest() throws Exception {
        PointSourceCreateRequestDto pointSourceCreateRequestDto = new PointSourceCreateRequestDto(
            null);

        String requestBody = objectMapper.writeValueAsString(pointSourceCreateRequestDto);

        mvc.perform(post("/api/shop/point/source/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());
    }

}
