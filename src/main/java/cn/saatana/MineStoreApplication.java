package cn.saatana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class MineStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(MineStoreApplication.class, args);
	}
}
