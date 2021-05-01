package org.brovushka.app.manager;

import org.brovushka.app.Application;
import org.brovushka.app.entity.ProductEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
public class ProductEntityManager
{
    public static ProductEntity getById(int id) throws SQLException
    {
        try(Connection c = Application.getConnection())
        {
            String sql = "SELECT * FROM Product INNER JOIN Manufacturer ON Product.ManufacturerID = Manufacturer.ID WHERE Product.ID=?";

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()) {
                return new ProductEntity(
                        resultSet.getInt("Product.ID"),
                        resultSet.getString("Title"),
                        resultSet.getDouble("Cost"),
                        resultSet.getString("Description"),
                        resultSet.getString("MainImagePath"),
                        resultSet.getInt("IsActive"),
                        resultSet.getInt("ManufacturerID"),
                        resultSet.getString("Name")
                );
            }

            return null;
        }
    }

    public static List<ProductEntity> getAll() throws SQLException
    {
        try(Connection c = Application.getConnection())
        {
            String sql = "SELECT * FROM Product INNER JOIN Manufacturer ON Product.ManufacturerID = Manufacturer.ID";
            Statement s = c.createStatement();
            ResultSet resultSet = s.executeQuery(sql);

            List<ProductEntity> list = new ArrayList<>();
            while(resultSet.next()) {
                list.add(new ProductEntity(
                        resultSet.getInt("Product.ID"),
                        resultSet.getString("Title"),
                        resultSet.getDouble("Cost"),
                        resultSet.getString("Description"),
                        resultSet.getString("MainImagePath"),
                        resultSet.getInt("IsActive"),
                        resultSet.getInt("ManufacturerID"),
                        resultSet.getString("Name")
                ));
            }

            return list;
        }
    }

    public static ProductEntity add(ProductEntity product) throws SQLException
    {
        try(Connection c = Application.getConnection())
        {
            String sql = "INSERT INTO Product(Title, Cost, Description, MainImagePath, IsActive, ManufacturerID) VALUES(?,?,?,?,?,?)";

            PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, product.getTitle());
            ps.setDouble(2, product.getCost());
            ps.setString(3, product.getDescription());
            ps.setString(4, product.getMainImagePath());
            ps.setInt(5, product.isActive() ? 1 : 0);
            ps.setInt(6, product.getManufacturerID());

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if(keys.next()) {
                product.setId(keys.getInt(1));
                return product;
            }

            throw new SQLException("entity not added");
        }
    }

    public static void edit(ProductEntity product) throws SQLException
    {
        try(Connection c = Application.getConnection())
        {
            String sql = "UPDATE Product SET Title=?, Cost=?, Description=?, MainImagePath=?, IsActive=?, ManufacturerID=? WHERE ID=?";

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, product.getTitle());
            ps.setDouble(2, product.getCost());
            ps.setString(3, product.getDescription());
            ps.setString(4, product.getMainImagePath());
            ps.setInt(5, product.isActive() ? 1 : 0);
            ps.setInt(6, product.getManufacturerID());
            ps.setInt(7, product.getId());

            ps.executeUpdate();
        }
    }

    public static void delete(int id) throws SQLException
    {
        try(Connection c = Application.getConnection())
        {
            String sql = "DELETE FROM Product WHERE ID=?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public static void delete(ProductEntity product) throws SQLException
    {
        delete(product.getId());
    }
}
