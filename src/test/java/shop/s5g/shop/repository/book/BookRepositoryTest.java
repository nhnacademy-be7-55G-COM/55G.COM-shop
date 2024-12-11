package shop.s5g.shop.repository.book;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shop.s5g.shop.config.TestQueryFactoryConfig;

@DataJpaTest
@Import(TestQueryFactoryConfig.class)
public class BookRepositoryTest {
}
