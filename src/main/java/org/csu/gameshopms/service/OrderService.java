package org.csu.gameshopms.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.csu.gameshopms.entity.ItemDTO;
import org.csu.gameshopms.entity.Order;
import org.csu.gameshopms.entity.OrderDetailDTO;
import org.csu.gameshopms.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class OrderService {

    /**
     * 查询所有订单
     */
    @Autowired
    private OrderMapper orderMapper;

    public List<Order> getAllOrders() {
        return orderMapper.selectList(null);
    }

    /**
     * 点击详情查询
     * @param id
     * @return json
     */

    public OrderDetailDTO getOrderById(Long id) {

        System.out.println("查询订单 ID: " + id); // 打印日志

        Order order = orderMapper.selectById(id);

        List<ItemDTO> items = orderMapper.getOrderItems(id);

        return new OrderDetailDTO(order, items);

    }

    /**
     * 切换订单状态
     */
    public String updateOrderState(Long id) {
        UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id).eq("state", "0") // 仅当 state="0" 时更新
                .set("state", "1");

        int updatedRows = orderMapper.update(null, updateWrapper);
        return updatedRows > 0 ? "1" : "0";
    }

    /**
     * 根据查询条件搜索订单
     * @param query 查询条件（id 或者 name）
     * @return 订单列表
     */
    public List<Order> searchOrders(String query) {
        System.out.println("Searching orders with query: " + query);
        return orderMapper.searchOrders(query);
    }
}

