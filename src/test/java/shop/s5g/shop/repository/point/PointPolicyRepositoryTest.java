package shop.s5g.shop.repository.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import shop.s5g.shop.config.TestQueryFactoryConfig;
import shop.s5g.shop.dto.point.PointPolicyView;

@DataJpaTest
@Import(TestQueryFactoryConfig.class)
class PointPolicyRepositoryTest {
    @Autowired
    PointPolicyRepository repository;

    @Test
    @Sql("classpath:point-test.sql")
    void findAllPoliciesTest() {
        assertThat(repository.findAllPolicies())
            .hasSize(3).extracting("name")
            .containsExactly("회원가입", "구매적립", "리뷰");
    }

    @Test
    @Sql("classpath:point-test.sql")
    void findByNameTest() {
        Optional<PointPolicyView> result = repository.findByName("회원가입");

        assertTrue(result.isPresent());
        assertEquals("회원가입", result.get().getName());
    }
}
