package org.csu.gameshopms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("cart")
public class Cart {
    private int id;
    @TableField("user_id")
    private int userId;
    @TableField("item_id")
    private int productId;
    @TableField("edition_id")
    private int editionId;
    private double price;
    private int in_cart;
    private int add_count;
    private int is_selected;

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", userId=" + userId +
                ", productId=" + productId +
                ", editionId=" + editionId +
                ", price=" + price +
                ", in_cart=" + in_cart +
                ", add_count=" + add_count +
                ", is_selected=" + is_selected +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getEditionId() {
        return editionId;
    }

    public void setEditionId(int editionId) {
        this.editionId = editionId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getIn_cart() {
        return in_cart;
    }

    public void setIn_cart(int in_cart) {
        this.in_cart = in_cart;
    }

    public int getAdd_count() {
        return add_count;
    }

    public void setAdd_count(int add_count) {
        this.add_count = add_count;
    }

    public int getIs_selected() {
        return is_selected;
    }

    public void setIs_selected(int is_selected) {
        this.is_selected = is_selected;
    }
}


