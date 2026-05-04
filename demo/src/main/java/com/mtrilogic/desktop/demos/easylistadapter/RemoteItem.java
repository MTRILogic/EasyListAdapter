package com.mtrilogic.desktop.demos.easylistadapter;

import com.mtrilogic.desktop.easylistadapter.Item;

import javax.swing.*;
import java.awt.*;

public class RemoteItem extends Item<RemoteModel> {
    private final JLabel lblText = new JLabel();

    public RemoteItem() {
        super(RemoteModel.class);
        setBackground(Color.WHITE);
        lblText.setBackground(Color.GREEN);
        lblText.setOpaque(true);
        with(lblText).north(0).west(5).east(50).south(1);
    }

    @Override
    protected void onBindModel() {
        lblText.setBackground(selected ? Color.BLUE : Color.GREEN);
        lblText.setForeground(selected ? Color.WHITE : Color.BLACK);
        lblText.setText(model.getText());
    }

    @Override
    protected int onNewHeight(int height) {
        return 30;
    }
}
