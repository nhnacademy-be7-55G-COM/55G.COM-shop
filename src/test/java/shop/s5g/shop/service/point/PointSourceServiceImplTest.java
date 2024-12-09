package shop.s5g.shop.service.point;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.shop.dto.point.PointPolicyFormResponseDto;
import shop.s5g.shop.dto.point.PointSourceCreateRequestDto;
import shop.s5g.shop.entity.point.PointSource;
import shop.s5g.shop.repository.point.PointSourceRepository;
import shop.s5g.shop.service.point.impl.PointSourceServiceImpl;

@ExtendWith(MockitoExtension.class)
class PointSourceServiceImplTest {

    @Mock
    PointSourceRepository pointSourceRepository;

    @InjectMocks
    PointSourceServiceImpl service;

    @Test
    void createPointSourceTest() {
        PointSourceCreateRequestDto pointSourceCreateRequestDto = new PointSourceCreateRequestDto(
            "testName");

        when(pointSourceRepository.findBySourceName(
            pointSourceCreateRequestDto.pointSourceName())).thenReturn(
            Optional.empty());

        Assertions.assertThatCode(() -> service.createPointSource(pointSourceCreateRequestDto))
            .doesNotThrowAnyException();
    }

    @Test
    void getPointPolicyFormDataTest(){
        List<PointSource> allPonitSourceList = new ArrayList<>();
        allPonitSourceList.add(new PointSource("testName1"));
        List<PointPolicyFormResponseDto> expectedResult = new ArrayList<>();

        allPonitSourceList.stream().forEach(pointSource -> expectedResult.add(
            new PointPolicyFormResponseDto(pointSource.getId(), pointSource.getSourceName())));

        when(pointSourceRepository.findAll()).thenReturn(allPonitSourceList);

        List<PointPolicyFormResponseDto> actualResult = service.getPointPolicyFormData();

        assertEquals(expectedResult, actualResult);
    }
}
