package org.csu.gameshopms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.csu.gameshopms.entity.ItemDTO;
import org.csu.gameshopms.entity.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface OrderMapper extends BaseMapper<Order> {


    //详情查询
    @Select("SELECT i.name, i.price FROM order_items oi JOIN item i ON oi.item_id = i.id WHERE oi.order_id = #{id}")
    List<ItemDTO> getOrderItems(@Param("id") Long id);

    //搜索
    @Select("<script>"
            + "SELECT * FROM orders "
            + "WHERE 1=1 "
            + "<if test='query != null'>"
            + "<choose>"
            + "<when test='query.matches(\"^\\\\d+$\")'>"
            + "AND id = #{query} "
            + "</when>"
            + "<otherwise>"
            + "AND name LIKE CONCAT('%', #{query}, '%') "
            + "</otherwise>"
            + "</choose>"
            + "</if>"
            + "</script>")
    List<Order> searchOrders(@Param("query") String query);

}
