package org.csu.gameshopms.mapper;

import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Component;

@Component
public interface ExistingGamesMapper {
    @Delete("DELETE FROM existing_games WHERE user_id = #{userId}")
    void deleteByUserId(int id);
}
