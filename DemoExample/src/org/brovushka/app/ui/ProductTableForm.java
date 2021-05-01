package org.brovushka.app.ui;

import org.brovushka.app.entity.ManufacturerEntity;
import org.brovushka.app.entity.ProductEntity;
import org.brovushka.app.manager.ManufacturerEntityManager;
import org.brovushka.app.manager.ProductEntityManager;
import org.brovushka.app.util.BaseForm;
import org.brovushka.app.util.CustomTableModel;
import org.brovushka.app.util.DialogUtil;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductTableForm extends BaseForm
{
    private JPanel mainPanel;
    private JTable table;
    private JButton addButton;
    private JButton idSortButton;
    private JButton titleSortButton;
    private JButton costSortButton;
    private JComboBox isActiveFilterBox;
    private JComboBox manufacturerFilterBox;
    private JButton clearButton;
    private JButton helpButton;
    private JButton dealWithAuthorButton;

    private CustomTableModel<ProductEntity> model;

    private boolean idSort = true;
    private boolean titleSort = false;
    private boolean costSort = false;

    public ProductTableForm()
    {
        setContentPane(mainPanel);

        this.initTable();
        this.initBoxes();
        this.initButtons();

        setVisible(true);
    }

    private void initTable()
    {
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(50);

        try {
            model = new CustomTableModel<ProductEntity>(
                    ProductEntity.class,
                    new String[] { "ID", "Название", "Цена", "Описание", "Путь до картинки", "Картинка", "Активен?", "ID производителя", "Производитель" },
                    ProductEntityManager.getAll()
            );
            table.setModel(model);

            hideTableColumn("ID");
            hideTableColumn("Путь до картинки");
            hideTableColumn("ID производителя");

            table.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if(selectedRow != -1 && e.getKeyCode() == KeyEvent.VK_DELETE) {
                        if(DialogUtil.showConfirm(ProductTableForm.this, "Вы точно хотите удалить данную запись?")) {
                            try {
                                ProductEntityManager.delete(model.getRows().get(selectedRow));
                                model.getRows().remove(selectedRow);
                                model.fireTableDataChanged();

                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        }
                    }
                }
            });

            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int row = table.rowAtPoint(e.getPoint());
                    if(row != -1 && e.getClickCount() == 2) {
                        new EditProductForm(ProductTableForm.this, model.getRows().get(row));
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void hideTableColumn(String columnName)
    {
        TableColumn column = table.getColumn(columnName);
        column.setMinWidth(0);
        column.setMaxWidth(0);
        column.setPreferredWidth(0);
    }

    private void initButtons()
    {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddProductForm(ProductTableForm.this);
            }
        });

        idSortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.sort(new Comparator<ProductEntity>() {
                    @Override
                    public int compare(ProductEntity o1, ProductEntity o2) {
                        if(idSort) {
                            return Integer.compare(o2.getId(), o1.getId());
                        } else {
                            return Integer.compare(o1.getId(), o2.getId());
                        }
                    }
                });

                idSort = !idSort;
                titleSort = false;
                costSort = false;
            }
        });

        titleSortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.sort(new Comparator<ProductEntity>() {
                    @Override
                    public int compare(ProductEntity o1, ProductEntity o2) {
                        if(titleSort) {
                            return o2.getTitle().compareTo(o1.getTitle());
                        } else {
                            return o1.getTitle().compareTo(o2.getTitle());
                        }
                    }
                });

                idSort = false;
                titleSort = !titleSort;
                costSort = false;
            }
        });

        costSortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.sort(new Comparator<ProductEntity>() {
                    @Override
                    public int compare(ProductEntity o1, ProductEntity o2) {
                        if(costSort) {
                            return Double.compare(o2.getCost(), o1.getCost());
                        } else {
                            return Double.compare(o1.getCost(), o2.getCost());
                        }
                    }
                });

                idSort = false;
                titleSort = false;
                costSort = !costSort;
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isActiveFilterBox.setSelectedIndex(0);
                manufacturerFilterBox.setSelectedIndex(0);
            }
        });

        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogUtil.showInfo(ProductTableForm.this, "Редактирование - двойной клик\nУдаление - клик и DELETE");
            }
        });

        dealWithAuthorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DialogUtil.showInfo(ProductTableForm.this, "Связаться с разработчиком можно по email student08@exam.com");
            }
        });
    }

    private void initBoxes()
    {
        isActiveFilterBox.addItem("Выберите состояние");
        isActiveFilterBox.addItem("Активен");
        isActiveFilterBox.addItem("Не активен");

        manufacturerFilterBox.addItem("Выберите производителя");
        try {
            for(ManufacturerEntity m : ManufacturerEntityManager.getAll()) {
                manufacturerFilterBox.addItem(m);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        isActiveFilterBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    applyFilterBoxes();
                }
            }	
        });

        manufacturerFilterBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    applyFilterBoxes();
                }
            }
        });
    }

    private void applyFilterBoxes()
    {
        try {
            List<ProductEntity> allProducts = ProductEntityManager.getAll();

            if(isActiveFilterBox.getSelectedIndex() == 1) {
                allProducts.removeIf(productEntity -> !productEntity.isActive());
            } else if(isActiveFilterBox.getSelectedIndex() == 2) {
                allProducts.removeIf(productEntity -> productEntity.isActive());
            }

            if(manufacturerFilterBox.getSelectedIndex() != 0) {
                ManufacturerEntity selectedManufacturer = (ManufacturerEntity) manufacturerFilterBox.getSelectedItem();
                allProducts.removeIf(productEntity -> productEntity.getManufacturerID() != selectedManufacturer.getId());
            }

            model.setRows(allProducts);
            model.fireTableDataChanged();

            idSort = true;
            titleSort = false;
            costSort = false;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public int getFormWidth() {
        return 1200;
    }

    @Override
    public int getFormHeight() {
        return 700;
    }

    public CustomTableModel<ProductEntity> getModel() {
        return model;
    }
}
