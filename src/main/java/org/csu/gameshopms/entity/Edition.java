package org.csu.gameshopms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("item_edition")
public class Edition {
    private int id;
    @TableField("product_id")
    private int productId;
    @TableField("editionName")
    private String editionName;
    private double price;
    private int storage;


    @Override
    public String toString() {
        return "Edition {" +
                "id=" + id +
                ", productId=" + productId +
                ", editionName='" + editionName + '\'' +
                ", price=" + price +
                ", storage=" + storage +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getEditionName() {
        return editionName;
    }

    public void setEditionName(String editionName) {
        this.editionName = editionName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }
}
