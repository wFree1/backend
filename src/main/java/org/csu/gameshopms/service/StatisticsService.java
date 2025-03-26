package org.csu.gameshopms.service;

import org.csu.gameshopms.entity.CategorySalesDTO;
import org.csu.gameshopms.entity.SalesDataDTO;
import org.csu.gameshopms.mapper.OrderItemMapper;
import org.csu.gameshopms.mapper.StatisticsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Service
public class StatisticsService {

    @Autowired
    private StatisticsMapper statisticsMapper;


    public int getTodayOrders() {
        return statisticsMapper.countTodayOrders();
    }

    public Double getTodaySales() {
        return statisticsMapper.countTodaySales();
    }

    public int getLast7DaysOrders() {
        return  statisticsMapper.countLast7DaysOrders();
    }

    public Double getLast7DaysSales() {
        return statisticsMapper.countLast7DaysSales();
    }

    @Autowired
    private OrderItemMapper orderItemMapper;
    public String getTopGame() {
        return orderItemMapper.findTopGame();
    }


    // 获取近七天的每天订单量和销售额
    public List<SalesDataDTO> getLastSevenDaysData() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 获取今天的日期
        LocalDate today = LocalDate.now();
        String endDate = today.format(formatter);
        String startDate = today.minusDays(6).format(formatter); // 获取七天前的日期

        // 使用Mapper查询近七天的数据
        List<SalesDataDTO> salesData = statisticsMapper.getSalesDataForLastSevenDays(startDate, endDate);

        // 生成七天的日期列表
        List<String> dateList = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            dateList.add(today.minusDays(i).format(formatter));
        }

        // 将查询到的日期与生成的七天日期列表合并
        Map<String, SalesDataDTO> salesDataMap = salesData.stream()
                .collect(Collectors.toMap(SalesDataDTO::getDate, data -> data));

        List<SalesDataDTO> result = new ArrayList<>();
        for (String date : dateList) {
            SalesDataDTO data = salesDataMap.get(date);
            if (data == null) {
                // 如果某天没有订单，设置订单量和销售额为0
                result.add(new SalesDataDTO(date, 0, 0.0));
            } else {
                result.add(data);
            }
        }

        return result;
    }

    //获取之前所有订单里商品的类型和对应类型总个数
    public List<CategorySalesDTO> getCategorySalesData() {
        return statisticsMapper.getCategorySalesData();
    }
}
