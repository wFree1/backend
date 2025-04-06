package org.csu.gameshopms.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.util.List;

@Data
@TableName("item")
public class Product {
    @TableId(value="id",type = IdType.AUTO)
    private int id;
    private String name;
    private String category;
    private String type;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String picture1;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String picture2;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String picture3;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String picture4;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String picture5;

    private String description;
    private double price;
    private int storage;
    @TableField(exist = false)  // 重要！标记为非数据库字段
    private List<Comment> comments;
    @TableField(exist = false)
    private List<Edition> editions; // 新增 editions 字段

    @Override
    public String toString() {
        return "Product {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", type='" + type + '\'' +
                ", picture1='" + picture1 + '\'' +
                ", picture2='" + picture2 + '\'' +
                ", picture3='" + picture3 + '\'' +
                ", picture4='" + picture4 + '\'' +
                ", picture5='" + picture5 + '\'' +
                ", description='" + description + '\'' +
                ", price=" + String.format("¥%.2f", price) +
                ", storage=" + storage +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public String getPicture1() {
        return picture1;
    }


    public String getPicture3() {
        return picture3;
    }


    public String getPicture2() {
        return picture2;
    }

    public String getPicture5() {
        return picture5;
    }

    public String getPicture4() {
        return picture4;
    }

    public void setPicture1(String picture1) {
        this.picture1 = picture1;
    }

    public void setPicture2(String picture2) {
        this.picture2 = picture2;
    }

    public void setPicture3(String picture3) {
        this.picture3 = picture3;
    }

    public void setPicture4(String picture4) {
        this.picture4 = picture4;
    }

    public void setPicture5(String picture5) {
        this.picture5 = picture5;
    }

    public void setComments(List<Comment> comments) {this.comments = comments;}
    public void setEditions(List<Edition> editions) {this.editions = editions;}
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
    public List<Comment> getComments() { return comments;}
    public List<Edition> getEditions() {
        return editions;
    }
}
