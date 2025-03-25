package org.csu.gameshopms.mapper;

import org.apache.ibatis.annotations.Delete;

public interface ExistingGamesMapper {
    @Delete("DELETE FROM existing_games WHERE user_id = #{userId}")
    void deleteByUserId(int id);
}
