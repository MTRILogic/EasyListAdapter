package com.mtrilogic.desktop.demos.easylistadapter;

import com.mtrilogic.desktop.easylistadapter.Item;
import com.mtrilogic.desktop.easylistadapter.Model;

import javax.swing.*;
import java.awt.*;

public class LocalItem extends Item<LocalModel> {
    private final JLabel lblText = new JLabel();

    public LocalItem() {
        super(LocalModel.class);
        setBackground(Color.WHITE);
        lblText.setHorizontalAlignment(SwingConstants.RIGHT);
        lblText.setBackground(Color.CYAN);
        lblText.setOpaque(true);
        with(lblText).north(0).west(50).east(5).south(1);
    }

    @Override
    protected void onBindModel(JList<? extends Model> list) {
        lblText.setBackground(selected ? Color.BLUE : Color.CYAN);
        lblText.setForeground(selected ? Color.WHITE : Color.BLACK);
        lblText.setText(model.getText());
    }

    @Override
    protected int onNewHeight(int height) {
        return 50;
    }
}
