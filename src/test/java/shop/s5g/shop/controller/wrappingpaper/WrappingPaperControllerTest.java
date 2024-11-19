package shop.s5g.shop.controller.wrappingpaper;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
import shop.s5g.shop.dto.wrappingpaper.WrappingPaperResponseDto;
import shop.s5g.shop.exception.order.WrappingPaperDoesNotExistsException;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.order.WrappingPaperService;

@WebMvcTest(
    value = WrappingPaperController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import({TestSecurityConfig.class, AuthenticationAdvice.class})
@EnableAspectJAutoProxy
class WrappingPaperControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    WrappingPaperService wrappingPaperService;

    @SpyBean
    RestWebAdvice advice;

    @Test
    void fetchPapersWrongQueryStringTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/api/shop/wrapping-paper")
            .queryParam("scope", "afsf"))
            .andExpect(status().isBadRequest());
    }

    WrappingPaperResponseDto w1 = new WrappingPaperResponseDto(
        1L, true, "테스트1", 3000, "image.png"
    );

    WrappingPaperResponseDto w2 = new WrappingPaperResponseDto(
        2L, false, "테스트2", 4000, "image.jpg"
    );

    @Test
    void fetchPapersAllTest() throws Exception{
        when(wrappingPaperService.getAllPapers()).thenReturn(List.of(w1, w2));

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/wrapping-paper")
                .queryParam("scope", "all"))
            .andExpect(content().string(containsString("image.png")));

        verify(wrappingPaperService, times(1)).getAllPapers();
        verify(wrappingPaperService, never()).getAllActivePaper();
    }
    @Test
    void fetchPapersActiveTest() throws Exception{
        when(wrappingPaperService.getAllPapers()).thenReturn(List.of(w1));

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/wrapping-paper"))
            .andExpect(content().string(not(containsString("image.gif"))));

        verify(wrappingPaperService, times(1)).getAllActivePaper();
        verify(wrappingPaperService, never()).getAllPapers();
    }

    @Test
    void fetchPaperTest() throws Exception{
        when(wrappingPaperService.getPaperById(anyLong())).thenReturn(w1);

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/wrapping-paper/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("테스트1"))
            .andExpect(jsonPath("$.price").value(3000));

        verify(wrappingPaperService, times(1)).getPaperById(anyLong());
    }
    @Test
    void fetchPaperFailTest() throws Exception{
        when(wrappingPaperService.getPaperById(anyLong())).thenThrow(
            WrappingPaperDoesNotExistsException.class
        );

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/wrapping-paper/1"))
            .andExpect(status().isNotFound());

        verify(wrappingPaperService, times(1)).getPaperById(anyLong());
        verify(advice, times(1)).handleResourceNotFoundException(any());
    }

    String create = """
        {
            "name": "포장지!",
            "price": 3400,
            "imageName": "image.png"
        }
        """;
    String failCreate = """
        {
            "name": "포장지!",
            "price": -3400,
            "imageName": "image.png"
        }
        """;

    @Test
    void createPaperTest() throws Exception {
        when(wrappingPaperService.save(any())).thenReturn(w1);

        mvc.perform(MockMvcRequestBuilders.post("/api/shop/wrapping-paper")
            .contentType(MediaType.APPLICATION_JSON)
            .content(create))
            .andExpect(status().isCreated());

        verify(wrappingPaperService, times(1)).save(any());
    }

    @Test
    void createPaperFailTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/shop/wrapping-paper")
                .contentType(MediaType.APPLICATION_JSON)
                .content(failCreate))
            .andExpect(status().isBadRequest());

        verify(wrappingPaperService, never()).save(any());
        verify(advice, times(1)).handleBadRequestException(any());
    }

    @Test
    void deletePaperTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/api/shop/wrapping-paper/2"))
            .andExpect(status().isNoContent());

        verify(wrappingPaperService, times(1)).deactivateById(anyLong());
    }
    @Test
    void deletePaperFailTest() throws Exception {
        doThrow(WrappingPaperDoesNotExistsException.class).when(wrappingPaperService).deactivateById(anyLong());
        mvc.perform(MockMvcRequestBuilders.delete("/api/shop/wrapping-paper/2"))
            .andExpect(status().isNotFound());

        verify(wrappingPaperService, times(1)).deactivateById(anyLong());
        verify(advice, times(1)).handleResourceNotFoundException(any());
    }
}
