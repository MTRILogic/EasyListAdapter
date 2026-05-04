package com.mtrilogic.desktop.demos.easylistadapter;

import com.mtrilogic.desktop.easylistadapter.Model;

/**
 * Modelo de ejemplo que usa ID automático (tipo 0).
 * <p>
 * El ID se genera automáticamente de forma ascendente al llamar a super(0).
 */
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
