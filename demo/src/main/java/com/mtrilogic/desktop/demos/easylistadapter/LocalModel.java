package com.mtrilogic.desktop.demos.easylistadapter;

import com.mtrilogic.desktop.easylistadapter.Model;

public class LocalModel extends Model {
    private String text;

    public LocalModel() {
        super(0); // Id automático, tipo 0
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
