package org.csu.gameshopms.persistence;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.csu.gameshopms.domian.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT COUNT(*) FROM user WHERE username LIKE CONCAT('%', #{query}, '%')")
    int getTotalUserCount(@Param("query") String query);

    @Select("SELECT * FROM user WHERE username LIKE CONCAT('%', #{query}, '%') LIMIT #{offset}, #{limit}")
    List<User> selectUsersWithPagination(
            @Param("query") String query,
            @Param("offset") int offset,
            @Param("limit") int limit
    );

    User selectById(int id);


    int insert(User user);

    void deleteById(int id);

    @Select("SELECT * FROM user WHERE id = #{id} FOR UPDATE")
    User selectByIdForUpdate(int userId);

}
