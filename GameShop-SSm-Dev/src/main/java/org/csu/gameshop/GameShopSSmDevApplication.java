package org.csu.gameshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.csu.gameshop.persistence")
public class GameShopSSmDevApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameShopSSmDevApplication.class, args);
    }

}
