package org.brovushka.app;

import org.brovushka.app.ui.ProductTableForm;
import org.brovushka.app.util.BaseForm;
import org.brovushka.app.util.DialogUtil;
import org.brovushka.app.util.FontUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Application
{
    private static Application instance;

    public static void main(String[] args) throws SQLException
    {
        new Application();
    }

    private Application()
    {
        instance = this;

        this.initDatabase();
        this.initUI();

        new ProductTableForm();
    }

    private void initDatabase()
    {
        try(Connection c = getConnection()) {
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtil.showError("Ошибка подключения к бд");
            System.exit(100);
        }
    }

    private void initUI()
    {
        BaseForm.setBaseApplicationTitle("Салон красоты бровушка");

        try {
            BaseForm.setBaseApplicationIcon(ImageIO.read(Application.class.getClassLoader().getResource("beauty_logo.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        FontUtil.changeAllFonts(new FontUIResource("Comic Sans MS", Font.TRUETYPE_FONT, 12));
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3304/mydb?serverTimeZone=UTC&characterEncoding=utf8", "root", "12345");
    }

    public static Application getInstance() {
        return instance;
    }
}
