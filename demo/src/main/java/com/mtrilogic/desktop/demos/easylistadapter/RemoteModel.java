package com.mtrilogic.desktop.demos.easylistadapter;

import com.mtrilogic.desktop.easylistadapter.Model;

/**
 * Modelo de ejemplo que usa ID automático (tipo 1).
 * <p>
 * El ID se genera automáticamente de forma ascendente al llamar a super(1).
 */
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
