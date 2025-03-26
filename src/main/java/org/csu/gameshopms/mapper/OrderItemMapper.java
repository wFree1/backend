package org.csu.gameshopms.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public interface OrderItemMapper {
    @Select("""
        SELECT i.name 
        FROM order_items oi
        JOIN item i ON oi.item_id = i.id
        GROUP BY oi.item_id
        ORDER BY COUNT(oi.item_id) DESC
        LIMIT 1
    """)
    String findTopGame();
}
