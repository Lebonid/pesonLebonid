package org.brovushka.app.util;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class BaseSubForm<T extends BaseForm> extends BaseForm
{
    protected final T mainForm;

    public BaseSubForm(T mainForm)
    {
        this.mainForm = mainForm;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                closeSubForm();
            }
        });

        mainForm.setEnabled(false);
    }

    public void closeSubForm()
    {
        dispose();
        mainForm.setEnabled(true);

        //костыль чтобы окно открылось поверх остальных
        mainForm.setVisible(false);
        mainForm.setVisible(true);
    }

    public T getMainForm() {
        return mainForm;
    }
}