package shop.s5g.shop.service.point;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.shop.dto.point.PointPolicyCreateRequestDto;
import shop.s5g.shop.dto.point.PointPolicyRemoveRequestDto;
import shop.s5g.shop.dto.point.PointPolicyUpdateRequestDto;
import shop.s5g.shop.dto.point.PointPolicyView;
import shop.s5g.shop.entity.point.PointPolicy;
import shop.s5g.shop.entity.point.PointSource;
import shop.s5g.shop.exception.EssentialDataNotFoundException;
import shop.s5g.shop.repository.point.PointPolicyRepository;
import shop.s5g.shop.repository.point.PointSourceRepository;
import shop.s5g.shop.service.point.impl.PointPolicyServiceImpl;

@ExtendWith(MockitoExtension.class)
class PointPolicyServiceImplTest {
    @Mock
    PointPolicyRepository pointPolicyRepository;

    @Mock
    PointSourceRepository pointSourceRepository;

    @InjectMocks
    PointPolicyServiceImpl service;

    @Test
    void getAllPoliciesTest() {
        assertThatCode(() -> service.getAllPolicies())
            .doesNotThrowAnyException();
    }

    @Test
    void getPolicyTest() {
        PointPolicyView mockView = mock(PointPolicyView.class);
        when(pointPolicyRepository.findByName(anyString())).thenReturn(Optional.of(mockView));

        assertThatCode(() -> service.getPolicy("asdf"))
            .doesNotThrowAnyException();

        verify(pointPolicyRepository, times(1)).findByName(anyString());
    }
    @Test
    void getPolicyFailTest() {
        when(pointPolicyRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getPolicy("asdf"))
            .isInstanceOf(EssentialDataNotFoundException.class);

        verify(pointPolicyRepository, times(1)).findByName(anyString());
    }

    @Test
    void updatePolicyValueTest() {
        PointPolicyUpdateRequestDto pointPolicyUpdateRequestDto = new PointPolicyUpdateRequestDto(
            1l, BigDecimal.valueOf(5000l));
        PointPolicy pointPolicy = mock(PointPolicy.class);

        when(pointPolicyRepository.findById(pointPolicyUpdateRequestDto.id())).thenReturn(
            Optional.of(pointPolicy));

        assertThatCode(() -> service.updatePolicyValue(
            pointPolicyUpdateRequestDto)).doesNotThrowAnyException();
    }

    @Test
    void createPointPolicyTest() {
        PointPolicyCreateRequestDto pointPolicyCreateRequestDto = new PointPolicyCreateRequestDto(
            "testName", "rate", BigDecimal.valueOf(0.1), 1l);
        PointPolicy pointPolicy = mock(PointPolicy.class);
        PointSource pointSource = mock(PointSource.class);

        when(pointPolicyRepository.findByName(pointPolicyCreateRequestDto.policyName())).thenReturn(
            Optional.empty());
        when(
            pointSourceRepository.findById(pointPolicyCreateRequestDto.pointSourceId())).thenReturn(
            Optional.of(pointSource));

        when(pointPolicyRepository.save(any())).thenReturn(pointPolicy);

        assertThatCode(() -> service.createPointPolicy(
            pointPolicyCreateRequestDto)).doesNotThrowAnyException();

    }

    @Test
    void createPointPolicyExceptionTest() {
        PointPolicyCreateRequestDto pointPolicyCreateRequestDto = new PointPolicyCreateRequestDto(
            "testName", "rate", BigDecimal.valueOf(0.1), 1l);
        PointPolicyView pointPolicyView = mock(PointPolicyView.class);

        when(pointPolicyRepository.findByName(pointPolicyCreateRequestDto.policyName())).thenReturn(
            Optional.of(pointPolicyView));

        assertThatThrownBy(() -> service.createPointPolicy(pointPolicyCreateRequestDto));

    }

    @Test
    void removePointPolicyTest() {
        PointPolicyRemoveRequestDto pointPolicyRemoveRequestDto = new PointPolicyRemoveRequestDto(
            "sourceName", 1l);

        doNothing().when(pointPolicyRepository).deleteById(pointPolicyRemoveRequestDto.id());

        assertThatCode(() -> service.removePointPolicy(
            pointPolicyRemoveRequestDto)).doesNotThrowAnyException();
    }

}
