package org.csu.gameshopms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("item")
public class Product {
    @TableId(value="id",type = IdType.AUTO)
    private int id;
    private String name;
    private String category;
    private String type;
    private String picture;
    private String description;
    private double price;
    private int storage;
    @Override
    public String toString() {
        return "Product {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", picture='" + picture + '\'' +
                ", description='" + description + '\'' +
                ", price=" + String.format("¥%.2f", price) + // 格式化为货币
                ", storage=" + storage +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
    public String getPicture() {
        return picture;
    }

    public String getName() {
        return name;
    }
    public double getPrice() {
        return price;
    }
    public String getCategory(){
        return category;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public int getStorage() {
        return storage;
    }
}
