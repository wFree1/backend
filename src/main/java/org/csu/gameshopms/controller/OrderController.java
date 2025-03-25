package org.csu.gameshopms.controller;

import org.csu.gameshopms.entity.Order;
import org.csu.gameshopms.entity.OrderDetailDTO;
import org.csu.gameshopms.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/main")
    public ResponseEntity<List<Order>> getOrders() {
        System.out.println("orderService: " + orderService);
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders); // 返回订单列表的 JSON 数据
    }

    // 订单详情页
    @GetMapping(value = "/detail/{id}",produces = "application/json")
    @ResponseBody()
    public ResponseEntity<OrderDetailDTO> getOrderDetail(@PathVariable Long id) {
        OrderDetailDTO orderDetail = orderService.getOrderById(id);
        if (orderDetail != null) {
            return ResponseEntity.ok(orderDetail);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    //状态修改
    @PutMapping("/ship/{id}")
    public Map<String, String> shipOrder(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        response.put("status", orderService.updateOrderState(id));
        return response;
    }
    /**
     * 搜索订单
     * @param query 查询条件（id 或者 name）
     * @return 订单列表
     */
    @GetMapping("/search")
    public List<Order> searchOrders(@RequestParam("query") String query) {
        // 打印接收到的查询参数
        System.out.println("Received query: " + query);
        return orderService.searchOrders(query);
    }
}