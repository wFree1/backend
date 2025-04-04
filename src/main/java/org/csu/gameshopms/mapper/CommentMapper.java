package org.csu.gameshopms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.csu.gameshopms.entity.Comment;

import java.util.List;

public interface CommentMapper extends BaseMapper<Comment> {
    // 自定义查询方法
    @Select("SELECT * FROM comment WHERE product_id = #{productId}")
    List<Comment> selectByProductId(@Param("productId") Integer productId);
    // 根据商品ID删除评论
    @Delete("DELETE FROM comment WHERE product_id = #{productId}")
    void deleteByProductId(@Param("productId") Integer productId);


}
