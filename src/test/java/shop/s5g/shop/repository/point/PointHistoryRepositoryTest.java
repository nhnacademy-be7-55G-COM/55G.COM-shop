package shop.s5g.shop.repository.point;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import shop.s5g.shop.config.TestQueryFactoryConfig;
import shop.s5g.shop.dto.point.PointHistoryResponseDto;

@DataJpaTest
@ActiveProfiles({"test", "embed-db"})
@Import(TestQueryFactoryConfig.class)
class PointHistoryRepositoryTest {
    @Autowired
    PointHistoryRepository pointHistoryRepository;

    @Test
    @Sql("classpath:point-test.sql")
    void findPointHistoryByMemberIdTest() {
        Pageable pageRequest = PageRequest.of(1, 4);

        Page<PointHistoryResponseDto> result = pointHistoryRepository.findPointHistoryByMemberId(1L, pageRequest);
        assertThat(result).hasSize(4)
            .element(0)
            .hasFieldOrPropertyWithValue("id", 6L);  // 8번 정보가 삭제되어 6,5,4,3번이 들어온 것인지 확인.

        assertThat(result.getTotalElements()).isEqualTo(10);
        assertThat(result.getTotalPages()).isEqualTo(3);
    }
}
