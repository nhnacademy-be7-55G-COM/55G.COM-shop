package shop.S5G.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = "shop.S5G.shop.service")
public class ShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopApplication.class, args);
	}

}
