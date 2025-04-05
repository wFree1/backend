package org.csu.gameshopms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.csu.gameshopms.entity.Comment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
@Component
@Repository()
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
    // 自定义查询方法
    @Select("SELECT c.*, u.username AS userName " +
            "FROM comment c " +
            "LEFT JOIN user u ON c.user_id = u.id " +
            "WHERE c.product_id = #{productId}")
    List<Comment> selectByProductId(@Param("productId") Integer productId);
    // 根据商品ID删除评论
    @Delete("DELETE FROM comment WHERE product_id = #{productId}")
    void deleteByProductId(@Param("productId") Integer productId);

    @Insert(
            "INSERT INTO comment (content, user_id, product_id, `like`, create_time) " +
            "VALUES (#{content}, #{user_id}, #{product_id}, #{like}, #{create_time})")
    int insertComment(Comment comment);

}
