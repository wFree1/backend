package org.csu.gameshopms.controller;

import org.csu.gameshopms.entity.CategorySalesDTO;
import org.csu.gameshopms.entity.SalesDataDTO;
import org.csu.gameshopms.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;


    @GetMapping
    public ResponseEntity<Map<String, Object>> getStatistics() {
        Map<String, Object> response = new HashMap<>();

        // 今日订单量
        int ordersOfToday = statisticsService.getTodayOrders();

        //今日销售额
        Double salesOfToday = statisticsService.getTodaySales();

        // 七日内订单总数
        int last7DaysOrders = statisticsService.getLast7DaysOrders();

        //七日内订单总销售额
        Double last7DaysSales = statisticsService.getLast7DaysSales();

        // 最畅销的游戏名称
        String topGameName = statisticsService.getTopGame();

        response.put("ordersOfToday", ordersOfToday);  // 返回的今日订单量
        response.put("salesOfToday", salesOfToday);  // 返回的今日销售额
        response.put("last7DaysOrders", last7DaysOrders);   //返回的七日订单量
        response.put("last7DaysSales", last7DaysSales);  //返回的七日销售额
        response.put("topGame", topGameName);   //返回畅销游戏
        response.put("message", "数据获取成功");
        response.put("status", 200);

        return ResponseEntity.ok(response);
    }

    // 获取近七天每天的订单量和销售额
    @GetMapping("/sales/last-seven-days")
    public ResponseEntity<List<SalesDataDTO>> getLastSevenDaysSales() {
        List<SalesDataDTO> salesData = statisticsService.getLastSevenDaysData();
        return ResponseEntity.ok(salesData);
    }

    // 获取近七天每天的游戏类别销售情况
    @GetMapping("/sales/category")
    public ResponseEntity<List<CategorySalesDTO>> getCategorySales() {
        List<CategorySalesDTO> salesData = statisticsService.getCategorySalesData();
        return ResponseEntity.ok(salesData);
    }

}
