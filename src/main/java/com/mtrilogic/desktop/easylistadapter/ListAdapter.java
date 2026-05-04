package com.mtrilogic.desktop.easylistadapter;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Adapter para {@link JList} que implementa el patrón RecyclerView de Android.
 * <p>
 * Esta clase actúa como puente entre los datos (modelos) y su representación visual
 * en una lista. Gestiona eficientemente la creación y reutilización de {@link Item}
 * según su tipo, evitando la creación excesiva de objetos y mejorando el rendimiento.
 * <p>
 * El mecanismo de reutilización funciona manteniendo una instancia de cada tipo
 * de item ({@code itemType}) y actualizando sus datos mediante {@link Item#bindModel(Model, int, boolean, boolean)}.
 * Esto es similar al patrón ViewHolder de Android.
 *
 * @see ListCellRenderer
 * @see Item
 * @see Model
 * @see Listener
 */
public class ListAdapter implements ListCellRenderer<Model> {

    /**
     * Mapa que almacena una instancia de Item por cada tipo.
     * <p>
     * La clave es el {@code itemType} del modelo, y el valor es la instancia
     * reutilizable de {@link Item} correspondiente.
     */
    private final Map<Integer, Item<? extends Model>> itemMap = new HashMap<>();

    /** Listener que proporciona nuevas instancias de Item cuando se requieren. */
    private final Listener listener;

    /**
     * Crea un nuevo ListAdapter con el listener especificado.
     *
     * @param listener Implementación de {@link Listener} que creará los Items.
     *                 No puede ser null.
     */
    public ListAdapter(Listener listener) {
        this.listener = listener;
    }

    /**
     * Renderiza una celda de la lista con el modelo proporcionado.
     * <p>
     * Flujo de renderizado:
     * <ol>
     *   <li>Obtiene el tipo del modelo mediante {@link Model#getItemType()}</li>
     *   <li>Busca un Item existente del mismo tipo en el caché</li>
     *   <li>Si no existe, solícita uno nuevo al {@link #listener}</li>
     *   <li>Enlaza el modelo al Item para actualizar su contenido visual</li>
     * </ol>
     *
     * @param list La JList que contiene este item
     * @param model El modelo de datos a mostrar
     * @param position Posición del item en la lista
     * @param isSelected {@code true} si el item está seleccionado
     * @param cellHasFocus {@code true} si el item tiene el foco
     * @return Component visual configurado para mostrar el modelo
     */
    @Override
    public Component getListCellRendererComponent(JList<? extends Model> list, Model model, int position, boolean isSelected, boolean cellHasFocus) {
        int itemType = model.getItemType();
        Item<? extends Model> item;
        if (itemMap.containsKey(itemType)) {
            item = itemMap.get(itemType);
        } else {
            item = listener.getItem(itemType);
            itemMap.put(itemType, item);
        }
        item.bindModel(model, position, isSelected, cellHasFocus);
        return item;
    }

    /**
     * Interface que debe implementar el contenedor para proveer Items según el tipo.
     * <p>
     * Esta interface es el equivalente a {@code RecyclerView.Adapter} en Android.
     * El método {@link #getItem(int)} actúa como factory que crea nuevas instancias
     * de {@link Item} cuando {@link ListAdapter} encuentra un tipo que aún no
     * tiene en su caché.
     *
     * @see ListAdapter
     * @see Item
     */
    public interface Listener {

        /**
         * Crea y retorna un nuevo Item del tipo especificado.
         * <p>
         * Este método es invocado cuando {@link ListAdapter} encuentra un tipo
         * de item que aún no existe en su caché. Las instancias se reutilizan
         * posteriormente mediante {@link Item#bindModel}.
         *
         * @param itemType Tipo de item (ej.: 0 para encabezados, 1 para contenido)
         * @return Nueva instancia de {@link Item} para el tipo especificado
         */
        Item<? extends Model> getItem(int itemType);
    }
}
