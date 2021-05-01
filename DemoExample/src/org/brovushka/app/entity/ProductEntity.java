package org.brovushka.app.entity;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/*
CREATE TABLE IF NOT EXISTS `Product` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `Title` VARCHAR(100) CHARACTER SET 'utf8mb4' NOT NULL,
  `Cost` DECIMAL(19,4) NOT NULL,
  `Description` LONGTEXT CHARACTER SET 'utf8mb4' NULL,
  `MainImagePath` VARCHAR(1000) CHARACTER SET 'utf8mb4' NULL,
  `IsActive` TINYINT(1) NOT NULL,
  `ManufacturerID` INT NULL,
 */
public class ProductEntity
{
    private int id;
    private String title;
    private double cost;
    private String description;
    private String mainImagePath;
    private ImageIcon image;
    private Boolean isActive;
    private int manufacturerID;
    private String manufacturer;

    public ProductEntity(int id, String title, double cost, String description, String mainImagePath, int isActive, int manufacturerID, String manufacturer) {
        this.id = id;
        this.title = title;
        this.cost = cost;
        this.description = description;
        this.mainImagePath = mainImagePath;
        this.isActive = isActive == 1;
        this.manufacturerID = manufacturerID;
        this.manufacturer = manufacturer;

        this.updateImage();
    }

    public ProductEntity(String title, double cost, String description, String mainImagePath, int isActive, int manufacturerID, String manufacturer) {
        this(-1, title, cost, description, mainImagePath, isActive, manufacturerID, manufacturer);
    }

    private void updateImage()
    {
        try {
            URL url = ProductEntity.class.getClassLoader().getResource(mainImagePath);
            if(url != null) {
                this.image = new ImageIcon(ImageIO.read(url));
            } else {
                this.image = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity that = (ProductEntity) o;
        return id == that.id && Double.compare(that.cost, cost) == 0 && manufacturerID == that.manufacturerID && Objects.equals(title, that.title) && Objects.equals(description, that.description) && Objects.equals(mainImagePath, that.mainImagePath) && Objects.equals(isActive, that.isActive) && Objects.equals(manufacturer, that.manufacturer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, cost, description, mainImagePath, isActive, manufacturerID, manufacturer);
    }

    @Override
    public String toString() {
        return "ProductEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", cost=" + cost +
                ", description='" + description + '\'' +
                ", mainImagePath='" + mainImagePath + '\'' +
                ", isActive=" + isActive +
                ", manufacturerID=" + manufacturerID +
                ", manufacturer='" + manufacturer + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMainImagePath() {
        return mainImagePath;
    }

    public void setMainImagePath(String mainImagePath) {
        this.mainImagePath = mainImagePath;
        this.updateImage();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getManufacturerID() {
        return manufacturerID;
    }

    public void setManufacturerID(int manufacturerID) {
        this.manufacturerID = manufacturerID;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
