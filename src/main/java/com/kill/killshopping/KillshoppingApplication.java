package com.kill.killshopping;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = {"com.kill.killshopping.test.mapper","com.kill.killshopping.mapper"})
public class KillshoppingApplication {

	public static void main(String[] args) {
		SpringApplication.run(KillshoppingApplication.class, args);
	}

}
