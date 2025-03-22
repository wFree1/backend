package org.csu.gameshop;

import org.csu.gameshop.entity.Category;
import org.csu.gameshop.persistence.CategoryMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GameShopSSmDevApplicationTests {
    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    void contextLoads() {
        Category category=categoryMapper.selectById("action");
        System.out.println(category);
    }

}
