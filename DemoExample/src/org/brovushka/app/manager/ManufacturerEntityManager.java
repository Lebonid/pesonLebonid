package org.brovushka.app.manager;

import org.brovushka.app.Application;
import org.brovushka.app.entity.ManufacturerEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
CREATE TABLE IF NOT EXISTS `Manufacturer` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `Name` VARCHAR(100) CHARACTER SET 'utf8mb4' NOT NULL,
  `StartDate` DATE NULL,
 */

public class ManufacturerEntityManager
{
    public static ManufacturerEntity getById(int id) throws SQLException
    {
        try(Connection c = Application.getConnection())
        {
            String sql = "SELECT * FROM Manufacturer WHERE ID=?";

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet resultSet = ps.executeQuery();
            if(resultSet.next()) {
                return new ManufacturerEntity(
                        resultSet.getInt("ID"),
                        resultSet.getString("Name"),
                        resultSet.getTimestamp("StartDate")
                );
            }

            return null;
        }
    }

    public static List<ManufacturerEntity> getAll() throws SQLException
    {
        try(Connection c = Application.getConnection())
        {
            String sql = "SELECT * FROM Manufacturer";
            Statement s = c.createStatement();
            ResultSet resultSet = s.executeQuery(sql);

            List<ManufacturerEntity> list = new ArrayList<>();
            while(resultSet.next()) {
                list.add(new ManufacturerEntity(
                        resultSet.getInt("ID"),
                        resultSet.getString("Name"),
                        resultSet.getTimestamp("StartDate")
                ));
            }

            return list;
        }
    }

    public static ManufacturerEntity add(ManufacturerEntity manufacturer) throws SQLException
    {
        try(Connection c = Application.getConnection())
        {
            String sql = "INSERT INTO Manufacturer(Name, StartDate) VALUES(?,?)";

            PreparedStatement ps = c.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, manufacturer.getName());
            ps.setTimestamp(2, new Timestamp(manufacturer.getStartDate().getTime()));

            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if(keys.next()) {
                manufacturer.setId(keys.getInt(1));
                return manufacturer;
            }

            throw new SQLException("entity not added");
        }
    }

    public static void edit(ManufacturerEntity manufacturer) throws SQLException
    {
        try(Connection c = Application.getConnection())
        {
            String sql = "UPDATE Manufacturer SET Name=?, StartDate=? WHERE ID=?";

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, manufacturer.getName());
            ps.setTimestamp(2, new Timestamp(manufacturer.getStartDate().getTime()));
            ps.setInt(3, manufacturer.getId());

            ps.executeUpdate();
        }
    }

    public static void delete(int id) throws SQLException
    {
        try(Connection c = Application.getConnection())
        {
            String sql = "DELETE FROM Manufacturer WHERE ID=?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public static void delete(ManufacturerEntity manufacturer) throws SQLException
    {
        delete(manufacturer.getId());
    }
}
