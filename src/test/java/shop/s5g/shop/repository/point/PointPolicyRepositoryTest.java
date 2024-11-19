package shop.s5g.shop.repository.point;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import shop.s5g.shop.config.TestQueryFactoryConfig;

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
}
