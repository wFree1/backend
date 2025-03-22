package org.csu.gameshop.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

// ProductVO.java
@Data
@TableName("item")
public class ProductVO {
    @TableId(value="id")
    private int id;
    private String name;
    private String category;      // 商品大类
    private String type;          // 具体类型（优惠版/普通版）
    private String picture;       // 主图URL
    private String description;
    private Double price;
    @Override
    public String toString() {
        return "ProductVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
