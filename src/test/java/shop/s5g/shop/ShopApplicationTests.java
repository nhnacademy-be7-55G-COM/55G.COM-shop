package shop.s5g.shop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("disable-redis")
class ShopApplicationTests {

	@Test
	void contextLoads() {
	}

}
