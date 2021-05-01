package org.brovushka.app.entity;

import java.util.Date;
import java.util.Objects;

/*
CREATE TABLE IF NOT EXISTS `Manufacturer` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(100) CHARACTER SET 'utf8mb4' NOT NULL,
  `StartDate` DATE NULL,
 */
public class ManufacturerEntity
{
    private int id;
    private String name;
    private Date startDate;

    public ManufacturerEntity(int id, String name, Date startDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
    }

    public ManufacturerEntity(String name, Date startDate) {
        this(-1, name, startDate);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManufacturerEntity that = (ManufacturerEntity) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(startDate, that.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, startDate);
    }

    @Override
    public String toString() {
        return this.name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
