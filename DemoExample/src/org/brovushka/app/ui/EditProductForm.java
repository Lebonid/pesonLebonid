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

public class EditProductForm extends BaseSubForm<ProductTableForm>
{
    private JPanel mainPanel;
    private JTextField idField;
    private JTextField titleField;
    private JTextField costField;
    private JTextField descField;
    private JTextField imgField;
    private JComboBox manufacturerBox;
    private JCheckBox isActiveCheckBox;
    private JButton saveButton;
    private JButton backButton;

    private ProductEntity product;

    public EditProductForm(ProductTableForm mainForm, ProductEntity product)
    {
        super(mainForm);
        this.product = product;

        setContentPane(mainPanel);

        this.initFields();
        this.initButtons();

        setVisible(true);
    }

    private void initFields()
    {
        idField.setText(String.valueOf(product.getId()));
        idField.setEditable(false);
        titleField.setText(product.getTitle());
        costField.setText(String.valueOf(product.getCost()));
        descField.setText(product.getDescription());
        imgField.setText(product.getMainImagePath());
        isActiveCheckBox.setSelected(product.isActive());

        try {

            ManufacturerEntity selectedManufacturer = null;
            for(ManufacturerEntity m : ManufacturerEntityManager.getAll()) {
                manufacturerBox.addItem(m);
                if(m.getId() == product.getManufacturerID()) {
                    selectedManufacturer = m;
                }
            }
            manufacturerBox.setSelectedItem(selectedManufacturer);

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

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                /*
                    опять же должны быть проверки на корректность полей
                    аналогично как в AddProductForm
                 */

                product.setTitle(titleField.getText());
                product.setCost(Double.parseDouble(costField.getText()));
                product.setDescription(descField.getText());
                product.setMainImagePath(imgField.getText());
                product.setActive(isActiveCheckBox.isSelected());

                ManufacturerEntity manufacturer = (ManufacturerEntity)manufacturerBox.getSelectedItem();
                product.setManufacturerID(manufacturer.getId());
                product.setManufacturer(manufacturer.getName());

                try {
                    ProductEntityManager.edit(product);
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
        return 350;
    }
}
