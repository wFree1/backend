package org.csu.gameshopms;

import org.csu.gameshopms.controller.OrderController;
import org.csu.gameshopms.controller.ProductController;
import org.csu.gameshopms.controller.UserController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GameShopMsSsmApplicationTests {

    @Autowired
    private ProductController productController;

    @Test
    void contextLoads() {
        System.out.println(productController.productDetail(23).toString());
    }

}
