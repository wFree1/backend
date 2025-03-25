package org.csu.gameshopms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.csu.gameshopms.persistence")
public class GameShopMsSsmApplication {

    public static void main(String[] args) {
        SpringApplication.run(GameShopMsSsmApplication.class, args);
    }

}
