package org.brovushka.app.ui;

import org.brovushka.app.entity.ManufacturerEntity;
import org.brovushka.app.entity.ProductEntity;
import org.brovushka.app.manager.ManufacturerEntityManager;
import org.brovushka.app.manager.ProductEntityManager;
import org.brovushka.app.util.BaseSubForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AddProductForm extends BaseSubForm<ProductTableForm>
{
    private JPanel mainPanel;
    private JTextField titleField;
    private JTextField costField;
    private JTextField descField;
    private JTextField imgField;
    private JComboBox manufacturerBox;
    private JCheckBox isActiveCheckBox;
    private JButton addButton;
    private JButton backButton;

    public AddProductForm(ProductTableForm mainForm)
    {
        super(mainForm);
        this.setContentPane(mainPanel);

        this.initFields();
        this.initButtons();

        this.setVisible(true);
    }

    private void initFields()
    {
        try {
            for(ManufacturerEntity m : ManufacturerEntityManager.getAll()) {
                manufacturerBox.addItem(m);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void initButtons()
    {
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeSubForm();
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String costString = costField.getText();
                double cost = -1;

                try {
                    cost = Double.parseDouble(costString);
                } catch (NumberFormatException ex) {
                    System.out.println("неверный формат цены");
                    return;
                }

                if(cost <= 0) {
                    System.out.println("цена введена неверно");
                    return;
                }

                /*
                    аналогичные проверки должны быть на остальных полях
                 */

                ManufacturerEntity manufacturer = (ManufacturerEntity)manufacturerBox.getSelectedItem();

                ProductEntity newProduct = new ProductEntity(
                        titleField.getText(),
                        cost,
                        descField.getText(),
                        imgField.getText(),
                        isActiveCheckBox.isSelected() ? 1 : 0,
                        manufacturer.getId(),
                        manufacturer.getName()
                );

                try {
                    ProductEntityManager.add(newProduct);
                    mainForm.getModel().getRows().add(newProduct);
                    mainForm.getModel().fireTableDataChanged();
                    closeSubForm();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getFormWidth() {
        return 600;
    }

    @Override
    public int getFormHeight() {
        return 320;
    }
}
