package org.csu.gameshopms.entity;

public class SalesDataDTO {
    private String date;  // 日期
    private int orderCount;  // 订单量
    private double salesAmount;  // 销售额

    // 构造函数
    public SalesDataDTO(String date, int orderCount, double salesAmount) {
        this.date = date;
        this.orderCount = orderCount;
        this.salesAmount = salesAmount;
    }

    // Getter 和 Setter
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public double getSalesAmount() {
        return salesAmount;
    }

    public void setSalesAmount(double salesAmount) {
        this.salesAmount = salesAmount;
    }
}
