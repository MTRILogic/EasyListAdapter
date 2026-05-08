package com.mtrilogic.desktop.demos.easylistadapter;

import com.mtrilogic.desktop.easylistadapter.Item;
import com.mtrilogic.desktop.easylistadapter.Model;

import javax.swing.*;
import java.awt.*;

public class RemoteItem extends Item<RemoteModel> {
    private final JLabel lblText = new JLabel();

    public RemoteItem() {
        super(RemoteModel.class);
        setBackground(Color.WHITE);
        lblText.setBackground(Color.GREEN);
        lblText.setOpaque(true);
        with(lblText).top(0).left(5).right(50).bottom(1);
    }

    @Override
    protected void onBindModel(JList<? extends Model> list) {
        lblText.setBackground(selected ? Color.BLUE : Color.GREEN);
        lblText.setForeground(selected ? Color.WHITE : Color.BLACK);
        lblText.setText(model.getText());
    }

    @Override
    protected int onNewHeight(int height) {
        return 30;
    }
}
