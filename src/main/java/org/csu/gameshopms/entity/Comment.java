package org.csu.gameshopms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
    @TableField("user_id")
    private Integer userId;
    @TableField("product_id")
    private Integer productId;
    private Integer like;
    @TableField("create_time")
    private LocalDateTime createTime;


    // 新增字段：userName（非数据库字段）
    @TableField(exist = false)  // 标注该字段不存在于数据库表中
    private String userName;

    public void setId(Integer id) {
        this.id = id;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public void setProductId(Integer productId) {
        this.productId = productId;
    }
    public void setLike(Integer like) {
        this.like = like;
    }
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public void setUserName(String userName) { this.userName = userName; }

    public Integer getId() {
        return id;
    }
    public String getContent() {
        return content;
    }
    public Integer getUserId() {
        return userId;
    }
    public Integer getProductId() {
        return productId;
    }
    public Integer getLike() {
        return like;
    }
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    public String getUserName() { return userName; }

}
