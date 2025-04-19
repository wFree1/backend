package org.csu.gameshopms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.csu.gameshopms.entity.Cart;
import org.csu.gameshopms.entity.Product;
import org.springframework.stereotype.Component;

@Component
public interface CartMapper extends BaseMapper<Cart> {
    @Delete("DELETE FROM cart WHERE user_id = #{userId}")
    void deleteByUserId(int id);
}
