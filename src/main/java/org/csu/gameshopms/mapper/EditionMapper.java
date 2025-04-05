package org.csu.gameshopms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.csu.gameshopms.entity.Edition;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository()
@Mapper
public interface EditionMapper extends BaseMapper<Edition> {
    @Insert({
            "<script>",
            "INSERT INTO item_edition (product_id, editionName, price, storage)",
            "VALUES ",
            "<foreach collection='list' item='item' separator=','>",
            "(",
            "#{item.productId}, #{item.editionName}, #{item.price}, #{item.storage}",
            ")",
            "</foreach>",
            "</script>"
    })
    int insertBatch(@Param("list") List<Edition> editions); // 使用 @Param("list") 绑定集合参数
}
