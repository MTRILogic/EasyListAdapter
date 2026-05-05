package com.mtrilogic.desktop.easylistadapter;

import com.mtrilogic.desktop.abstracts.SpringPanel;

import javax.swing.*;
import java.awt.*;

/**
 * Representa una vista individual dentro de una lista (equivalente a ViewHolder en Android).
 * <p>
 * Esta clase abstracta extiende {@link SpringPanel} y proporciona la estructura base
 * para crear items visuales que se muestran en un {@link EasyList}. Maneja automáticamente
 * el enlace con el modelo de datos y el estado visual (selección y foco).
 * <p>
 * Las subclases deben implementar {@link #onBindModel(JList)} para definir cómo se actualizan
 * los componentes visuales cuando el modelo cambia.
 * <p>
 * Ejemplo de implementación:
 * <pre>
 * public class MyItem extends Item<MyModel> {
 *     private final JLabel label = new JLabel();
 *
 *     public MyItem() {
 *         super(MyModel.class);
 *         with(label).north(5).west(5).east(5).south(5);
 *     }
 *
 *     {@literal @Override}
 *     protected void onBindModel(JList<? extends Model> list) {
 *         label.setText(model.getName());
 *         label.setBackground(selected ? BLUE : WHITE);
 *     }
 *
 *     {@literal @Override}
 *     protected int onNewHeight(int height) {
 *         return 50; // Altura fija de 50 píxeles
 *     }
 * }
 * </pre>
 *
 * @param <M> Tipo de modelo que este item puede mostrar, debe extender {@link Model}
 * @see EasyList
 * @see ListAdapter
 * @see Model
 */
public abstract class Item<M extends Model> extends SpringPanel {

    /** Clase del modelo para casting seguro en tiempo de ejecución */
    private final Class<M> clazz;

    /** Indica si este item tiene el foco actualmente */
    protected boolean focused;

    /** Indica si este item está seleccionado actualmente */
    protected boolean selected;

    /** Posición del item dentro de la lista */
    protected int position;

    /** Instancia del modelo enlazada a este item */
    protected M model;

    /**
     * Método abstracto que se invoca cuando el modelo es enlazado al item.
     * <p>
     * Las subclases deben implementar este método para actualizar sus
     * componentes visuales según los datos del {@link #model}, {@link #selected}
     * y {@link #focused}.
     *
     * @param list La lista a la que pertenece este item
     */
    protected abstract void onBindModel(JList<? extends Model> list);

    /**
     * Crea un nuevo Item con el tipo de modelo especificado.
     *
     * @param clazz Clase del modelo genérico M, necesaria para casting seguro
     */
    public Item(Class<M> clazz) {
        this.clazz = clazz;
    }

    /**
     * Enlaza un modelo a este item y actualiza su estado visual.
     * <p>
     * Este método es invocado por {@link ListAdapter} cuando el item
     * debe mostrar nuevos datos. Realiza el casting seguro del modelo
     * y actualiza las variables de estado antes de invocar {@link #onBindModel(JList)}.
     *
     * @param list La lista a la que pertenece este item
     * @param m Modelo a enlazar, debe ser compatible con el tipo M
     * @param position Posición del item dentro de la lista
     * @param selected {@code true} si el item está seleccionado
     * @param focused {@code true} si el item tiene el foco
     */
    public void bindModel(JList<? extends Model> list, Model m, int position, boolean selected, boolean focused) {
        model = clazz.cast(m);
        this.position = position;
        this.selected = selected;
        this.focused = focused;
        onBindModel(list);
    }

    /**
     * Calcula el tamaño preferido de este item.
     * <p>
     * Sobrescribe el comportamiento estándar para permitir que las subclases
     * definan una altura personalizada mediante {@link #onNewHeight(int)}.
     * El ancho se mantiene según el cálculo estándar de {@link SpringPanel}.
     *
     * @return Dimension con el ancho estándar y la altura personalizada
     */
    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        int height = onNewHeight(size.height);
        return new Dimension(size.width, height);
    }

    /**
     * Método de callback para definir la altura del item.
     * <p>
     * Las subclases pueden sobrescribir este método para retornar una altura
     * fija o calculada basada en el valor por defecto. Si no se sobrescribe,
     * retorna la altura calculada por defecto de {@link SpringPanel}.
     * <p>
     * Este método es invocado automáticamente durante {@link #getPreferredSize()}.
     *
     * @param height Altura calculada por defecto por SpringPanel
     * @return Altura final que debe tener este item
     */
    protected int onNewHeight(int height) {
        return height;
    }
}
