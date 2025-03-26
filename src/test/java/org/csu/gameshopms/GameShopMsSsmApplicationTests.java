package org.csu.gameshopms;

import org.csu.gameshopms.controller.OrderController;
import org.csu.gameshopms.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GameShopMsSsmApplicationTests {

    @Autowired
    private UserController userController;

    @Test
    void contextLoads() {
        System.out.println(userController.total());
    }

}
