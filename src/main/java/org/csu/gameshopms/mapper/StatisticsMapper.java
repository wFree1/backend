package org.csu.gameshopms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.csu.gameshopms.entity.CategorySalesDTO;
import org.csu.gameshopms.entity.Order;
import org.csu.gameshopms.entity.SalesDataDTO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
@Mapper
public interface StatisticsMapper extends BaseMapper<Order> {

    /**
     *自定义SQL数据查询
     */
    @Select("SELECT COUNT(*) FROM orders WHERE DATE(datetime) = CURDATE()")
    int countTodayOrders();

    @Select("SELECT SUM(total) FROM orders WHERE DATE(datetime) = CURDATE()")
    Double countTodaySales();

    // 查询最近 7 天订单量（包含今天）
    @Select("SELECT COUNT(*) FROM orders WHERE datetime >= DATE_SUB(CURDATE(), INTERVAL 6 DAY)")
    int countLast7DaysOrders();

    @Select("SELECT SUM(total) FROM orders WHERE datetime >= DATE_SUB(CURDATE(), INTERVAL 6 DAY)")
    Double countLast7DaysSales();


    // 获取过去七天每天的订单量和销售额
    @Select({
            "<script>",
            "SELECT DATE(datetime) AS order_date, COUNT(*) AS order_count, SUM(total) AS sales_amount ",
            "FROM orders ",
            "WHERE datetime BETWEEN #{startDate} AND #{endDate} ",
            "GROUP BY DATE(datetime) ",
            "ORDER BY DATE(datetime) ASC",
            "</script>"
    })
    List<SalesDataDTO> getSalesDataForLastSevenDays(@Param("startDate") String startDate, @Param("endDate") String endDate);

    //获取之前所有订单里商品的类型和对应类型总个数
    @Select({
            "SELECT i.category, COUNT(*) AS total_quantity",
            "FROM order_items oi",
            "JOIN item i ON oi.item_id = i.id",
            "JOIN orders o ON oi.order_id = o.id",
            "GROUP BY i.category",
            "ORDER BY total_quantity DESC"
    })
    List<CategorySalesDTO> getCategorySalesData();
}
