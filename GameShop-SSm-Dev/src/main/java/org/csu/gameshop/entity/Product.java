package org.csu.gameshop.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("item")
public class Product {
    @TableId(value="id")
    private int id;
    private String name;
    private String category;
    private String type;
    private String picture;
    private String description;
    private double price;
}
