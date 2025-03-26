package org.csu.gameshopms.mapper;

import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Component;

@Component
public interface CartMapper {
    @Delete("DELETE FROM cart WHERE user_id = #{userId}")
    void deleteByUserId(int id);
}
