package org.csu.gameshopms.entity;

public class CategorySalesDTO {
    private String category;  // 商品类别
    private Long totalQuantity; // 总购买数量

    public CategorySalesDTO(String category, Long totalQuantity) {
        this.category = category;
        this.totalQuantity = totalQuantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Long totalQuantity) {
        this.totalQuantity = totalQuantity;
    }
}
