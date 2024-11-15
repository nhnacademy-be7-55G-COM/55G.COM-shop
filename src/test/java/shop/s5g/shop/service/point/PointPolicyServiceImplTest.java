package shop.s5g.shop.service.point;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.shop.dto.point.PointPolicyView;
import shop.s5g.shop.exception.EssentialDataNotFoundException;
import shop.s5g.shop.repository.point.PointPolicyRepository;
import shop.s5g.shop.service.point.impl.PointPolicyServiceImpl;

@ExtendWith(MockitoExtension.class)
class PointPolicyServiceImplTest {
    @Mock
    PointPolicyRepository pointPolicyRepository;

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

}
