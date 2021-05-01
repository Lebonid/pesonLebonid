package org.brovushka.app.entity;

import java.util.Date;
import java.util.Objects;

/*
CREATE TABLE IF NOT EXISTS `ProductSale` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `SaleDate` DATETIME(6) NOT NULL,
  `ProductID` INT NOT NULL,
  `Quantity` INT NOT NULL,
 */
public class ProductSaleEntity
{
    private int id;
    private Date saleDate;
    private int productId;
    private int quantity;

    public ProductSaleEntity(int id, Date saleDate, int productId, int quantity) {
        this.id = id;
        this.saleDate = saleDate;
        this.productId = productId;
        this.quantity = quantity;
    }

    public ProductSaleEntity(Date saleDate, int productId, int quantity) {
        this(-1, saleDate, productId, quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductSaleEntity that = (ProductSaleEntity) o;
        return id == that.id && productId == that.productId && quantity == that.quantity && Objects.equals(saleDate, that.saleDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, saleDate, productId, quantity);
    }

    @Override
    public String toString() {
        return "ProductSaleEntity{" +
                "id=" + id +
                ", saleDate=" + saleDate +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(Date saleDate) {
        this.saleDate = saleDate;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
