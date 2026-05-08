package com.mtrilogic.desktop.demos.easylistadapter;

import com.mtrilogic.desktop.easylistadapter.Model;

public class RemoteModel extends Model {
    private String text;

    public RemoteModel() {
        super(1); // Id automático, tipo 1
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
