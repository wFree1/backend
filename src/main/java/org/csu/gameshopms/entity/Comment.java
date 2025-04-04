package org.csu.gameshopms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("comment")
public class Comment {
    @TableId(type= IdType.AUTO)
    private Integer id;
    private String content;
    private Integer user_id;
    private Integer product_id;
    private Integer like;
    private LocalDateTime create_time;

    public void setId(Integer id) {
        this.id = id;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }
    public void setLike(Integer like) {
        this.like = like;
    }
    public void setCreate_time(LocalDateTime create_time) {
        this.create_time = create_time;
    }

    public Integer getId() {
        return id;
    }
    public String getContent() {
        return content;
    }
    public Integer getUser_id() {
        return user_id;
    }
    public Integer getProduct_id() {
        return product_id;
    }
    public Integer getLike() {
        return like;
    }
    public LocalDateTime getCreate_time() {
        return create_time;
    }
}
