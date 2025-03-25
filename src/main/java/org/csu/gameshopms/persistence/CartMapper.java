package org.csu.gameshopms.persistence;

import org.apache.ibatis.annotations.Delete;

public interface CartMapper {
    @Delete("DELETE FROM cart WHERE user_id = #{userId}")
    void deleteByUserId(int id);
}
