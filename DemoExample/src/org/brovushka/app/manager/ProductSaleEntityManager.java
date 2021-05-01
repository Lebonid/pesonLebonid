package org.brovushka.app.manager;

import org.brovushka.app.Application;
import org.brovushka.app.entity.ProductSaleEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
CREATE TABLE IF NOT EXISTS `ProductSale` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `SaleDate` DATETIME(6) NOT NULL,
  `ProductID` INT NOT NULL,
  `Quantity` INT NOT NULL,
 */
public class ProductSaleEntityManager
{
   public static ProductSaleEntity getById(int id) throws SQLException
   {
       try(Connection c = Application.getConnection())
       {
           String sql = "SELECT * FROM ProductSale WHERE ID=?";

           PreparedStatement ps = c.prepareStatement(sql);
           ps.setInt(1, id);

           ResultSet resultSet = ps.executeQuery();
           if(resultSet.next()) {
               return new ProductSaleEntity(
                       resultSet.getInt("ID"),
                       resultSet.getTimestamp("SaleDate"),
                       resultSet.getInt("ProductID"),
                       resultSet.getInt("Quantity")
               );
           }

           return null;
       }
   }

    public static List<ProductSaleEntity> getAll() throws SQLException
    {
        try(Connection c = Application.getConnection())
        {
            String sql = "SELECT * FROM ProductSale";
            Statement s = c.createStatement();
            ResultSet resultSet = s.executeQuery(sql);

            List<ProductSaleEntity> list = new ArrayList<>();
            while(resultSet.next()) {
                list.add(new ProductSaleEntity(
                        resultSet.getInt("ID"),
                        resultSet.getTimestamp("SaleDate"),
                        resultSet.getInt("ProductID"),
                        resultSet.getInt("Quantity")
                ));
            }

            return list;
        }
    }

    public static ProductSaleEntity add(ProductSaleEntity productSale) throws SQLException
    {
        try(Connection c = Application.getConnection())
        {
            String sql = "INSERT INTO ProductSale(SaleDate, ProductID, Quantity) VALUES(?,?,?)";

            PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setTimestamp(1, new Timestamp(productSale.getSaleDate().getTime()));
            ps.setInt(2, productSale.getProductId());
            ps.setInt(3, productSale.getQuantity());

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if(keys.next()) {
                productSale.setId(keys.getInt(1));
                return productSale;
            }

            throw new SQLException("entity not added");
        }
    }

    public static void edit(ProductSaleEntity productSale) throws SQLException
    {
        try(Connection c = Application.getConnection())
        {
            String sql = "UPDATE ProductSale SaleDate=?, ProductID=?, Quantity=? WHERE ID=?";

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setTimestamp(1, new Timestamp(productSale.getSaleDate().getTime()));
            ps.setInt(2, productSale.getProductId());
            ps.setInt(3, productSale.getQuantity());
            ps.setInt(4, productSale.getId());

            ps.executeUpdate();
        }
    }

    public static void delete(int id) throws SQLException
    {
        try(Connection c = Application.getConnection())
        {
            String sql = "DELETE FROM ProductSale WHERE ID=?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public static void delete(ProductSaleEntity productSale) throws SQLException
    {
        delete(productSale.getId());
    }
}
