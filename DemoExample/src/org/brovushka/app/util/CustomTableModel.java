package org.brovushka.app.util;

import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CustomTableModel<T> extends AbstractTableModel
{
    /*
     нельзя путать class и Class
     class - тип данных впринципе системное слово
     Class - тип данных который описывает class
     */

    private final Class<T> cls; //объект который описывает тип данных
    private final String[] columnNames; //массив который хранит названия колонок
    private List<T> rows; //коллекция хранящая сущности таблицы

    public CustomTableModel(Class<T> cls, String[] columnNames, List<T> rows)
    {
        this.cls = cls;
        this.columnNames = columnNames;
        this.rows = rows;
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    //ограничение: UserEntity или любой другой класс который будет храниться в таблице
    //не должен содержать лишних полей, вообще, даже статичных
    @Override
    public int getColumnCount() {
        //получаем массив с зарегистрированнными полями в UserEntity и берем длину
        return cls.getDeclaredFields().length;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        //мы соотносим номер колонки в таблице с номером поля в классе
        //получаем также массив всех зарегистрированных полей
        //выбираем поле по номеру колонки и получаем Class этого поля
        return cls.getDeclaredFields()[columnIndex].getType();
    }

    /*@Override
    public String getColumnName(int column) {
        return cls.getDeclaredFields()[column].getName();
    }*/

    @Override
    public String getColumnName(int column) {
        return column >= columnNames.length ? "NAME" : columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            //получаем поле UserEntity по номеру колонки
            Field field = cls.getDeclaredFields()[columnIndex];

            //делаем его доступным (если оно private)
            field.setAccessible(true);

            //получаем значение этого поля из объекта
            return field.get(this.rows.get(rowIndex));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public void sort(Comparator<T> comparator) {
        Collections.sort(rows, comparator);
        fireTableDataChanged();
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
