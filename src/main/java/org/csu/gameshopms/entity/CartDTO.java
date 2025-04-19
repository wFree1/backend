package org.csu.gameshopms.entity;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CartDTO {
    private int userId;
    private int itemId;
    private int editionId;
    private double price;
    // getters & setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public int getEditionId() { return editionId; }
    public void setEditionId(int editionId) { this.editionId = editionId; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
