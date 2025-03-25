package org.csu.gameshopms.entity;

import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class OrderDetailDTO {
    private Order order;  // 订单基本信息
    private List<ItemDTO> items;  // 订单中的商品信息

    public OrderDetailDTO(Order order, List<ItemDTO> items) {
        this.order = order;
        this.items = items;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }
}
