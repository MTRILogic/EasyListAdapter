package com.mtrilogic.desktop.easylistadapter;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Componente {@link JList} simplificado con adapter tipo RecyclerView de Android.
 * <p>
 * Esta clase extiende {@link JList} para proporcionar una API simplificada para
 * crear listas con items de múltiples tipos y contenido variable. Integra
 * {@link ListAdapter} como renderizador de celdas, permitiendo el patrón ViewHolder
 * para reutilización eficiente de componentes.
 * <p>
 * Características principales:
 * <ul>
 *   <li>Gestión simplificada de items (agregar, insertar, remover, actualizar)</li>
 *   <li>Soporte para múltiples tipos de items mediante {@link Model#getItemType()}</li>
 *   <li>Reutilización de instancias {@link Item} para mejor rendimiento</li>
 *   <li>Método utilitario {@link #createScrollPane()} para envolver en scroll</li>
 * </ul>
 * <p>
 * Ejemplo de uso:
 * <pre>
 * EasyList list = new EasyList(new ListAdapter.Listener() {
 *     {@literal @Override}
 *     public Item&lt;? extends Model&gt; getItem(int itemType) {
 *         return (itemType == 0) ? new HeaderItem() : new ContentItem();
 *     }
 * });
 *
 * // Agregar items
 * easyList.appendItem(new MyModel(1, 0, "Título"));
 * easyList.appendItem(new MyModel(2, 1, "Contenido"));
 *
 * // Crear scroll pane
 * JScrollPane scroll = easyList.createScrollPane();
 * panel.add(scroll);
 * </pre>
 *
 * @see JList
 * @see ListAdapter
 * @see Model
 * @see Item
 * @see ListAdapter.Listener
 */
@SuppressWarnings("unused")
public class EasyList extends JList<Model> {

    /**
     * Modelo interno de Swing que almacena los elementos de la lista.
     * <p>
     * Se utiliza {@link DefaultListModel} para gestionar automáticamente
     * las notificaciones de cambios a los listeners de la lista.
     */
    private final DefaultListModel<Model> listModel = new DefaultListModel<>();

    /**
     * Almacena temporalmente el último modelo removido mediante {@link #removeItem(int)}.
     * <p>
     * Permite recuperar el modelo eliminado mediante {@link #getOldItem()} si es necesario.
     */
    private Model oldModel;

    /**
     * Crea un EasyList con el listener especificado.
     * <p>
     * Inicializa la lista configurando el {@link DefaultListModel} como modelo
     * de datos y creando un {@link ListAdapter} como renderizador de celdas.
     *
     * @param listener Implementación de {@link ListAdapter.Listener} que provee
     *                 instancias de {@link Item} según el tipo. No puede ser null.
     */
    public EasyList(ListAdapter.Listener listener) {
        ListAdapter adapter = new ListAdapter(listener);
        setModel(listModel);
        setCellRenderer(adapter);
    }

    /**
     * Agrega un modelo al final de la lista.
     * <p>
     * El modelo se añade después del último elemento existente.
     * Notifica automáticamente a los listeners registrados.
     *
     * @param model Modelo a agregar, no puede ser null
     */
    public void appendItem(Model model) {
        listModel.addElement(model);
    }

    /**
     * Inserta un modelo en la posición especificada.
     * <p>
     * Los elementos existentes desde la posición especificada se desplazan
     * hacia adelante. Si la posición es igual al tamaño actual, equivale
     * a {@link #appendItem(Model)}.
     *
     * @param position Posición de inserción (0-based, debe ser 0 <= position <= size)
     * @param model Modelo a insertar, no puede ser null
     * @throws ArrayIndexOutOfBoundsException si la posición es inválida
     */
    public void insertItem(int position, Model model) {
        listModel.add(position, model);
    }

    /**
     * Remueve el item en la posición especificada.
     * <p>
     * El modelo removido se almacena temporalmente y puede recuperarse
     * mediante {@link #getOldItem()}. Todos los elementos posteriores
     * se desplazan hacia atrás una posición.
     *
     * @param position Posición del item a remover (0-based)
     * @throws ArrayIndexOutOfBoundsException si la posición es inválida
     * @see #getOldItem()
     */
    public void removeItem(int position) {
        oldModel = listModel.remove(position);
    }

    /**
     * Remueve la primera ocurrencia del modelo especificado.
     *
     * @param model Modelo a buscar y remover
     * @return {@code true} si el modelo fue encontrado y removido,
     *         {@code false} si no existe en la lista
     */
    public boolean removeItem(Model model) {
        return listModel.removeElement(model);
    }

    /**
     * Remueve una cantidad específica de items comenzando desde una posición.
     *
     * @param startPosition Posición inicial (inclusive)
     * @param count Cantidad de items a remover
     * @throws ArrayIndexOutOfBoundsException si el rango excede los límites
     * @see #removeRangeItems(int, int)
     */
    public void removeItems(int startPosition, int count) {
        removeRangeItems(startPosition, startPosition + count);
    }

    /**
     * Remueve un rango de items de la lista.
     * <p>
     * Remueve todos los elementos desde {@code startPosition} hasta
     * {@code endPosition} (ambos inclusive).
     *
     * @param startPosition Posición inicial del rango (inclusive)
     * @param endPosition Posición final del rango (inclusive)
     * @throws ArrayIndexOutOfBoundsException si el rango es inválido
     */
    public void removeRangeItems(int startPosition, int endPosition) {
        listModel.removeRange(startPosition, endPosition);
    }

    /**
     * Busca la posición de un item por su {@code itemId}.
     * <p>
     * Este método permite encontrar la posición actual de un item
     * independientemente de si la lista ha sido filtrada, ordenada o modificada.
     * Útil para animaciones o tracking de items específicos.
     *
     * @param itemId Id único del item a buscar
     * @return Posición del item (0-based), o -1 si no se encuentra
     * @see Model#getItemId()
     */
    public int getPositionByItemId(long itemId) {
        int size = listModel.getSize();
        for (int i = 0; i < size; i++) {
            if (listModel.get(i).getItemId() == itemId) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Obtiene un modelo por su {@code itemId}.
     * <p>
     * Busca en toda la lista el item con el ID especificado y lo retorna
     * con casting de tipo seguro. Independiente de la posición actual.
     *
     * @param itemId Id único del item a buscar
     * @param clazz Clase para casting seguro
     * @param <M> Tipo del modelo
     * @return Modelo encontrado casteado al tipo especificado, o {@code null} si no existe
     * @see Model#getItemId()
     */
    public <M extends Model> M getItemById(long itemId, Class<M> clazz) {
        int size = listModel.getSize();
        for (int i = 0; i < size; i++) {
            Model model = listModel.get(i);
            if (model.getItemId() == itemId) {
                return clazz.cast(model);
            }
        }
        return null;
    }

    /**
     * Actualiza un item específico por su {@code itemId}.
     * <p>
     * Busca el item con el ID especificado y reemplaza su modelo.
     * El {@code itemId} del nuevo modelo se preserva automáticamente
     * si usas el constructor con ID manual.
     *
     * @param itemId Id del item a actualizar
     * @param newModel Nuevo modelo que reemplazará al existente
     * @return {@code true} si se encontró y actualizó, {@code false} si no existe
     * @see Model#getItemId()
     */
    public boolean updateItemById(long itemId, Model newModel) {
        int position = getPositionByItemId(itemId);
        if (position >= 0) {
            listModel.set(position, newModel);
            return true;
        }
        return false;
    }

    /**
     * Remueve un item específico por su {@code itemId}.
     * <p>
     * Busca y elimina el item con el ID especificado, independientemente
     * de su posición actual en la lista.
     *
     * @param itemId ID del item a remover
     * @return {@code true} si se encontró y removió, {@code false} si no existe
     * @see Model#getItemId()
     */
    public boolean removeItemById(long itemId) {
        int position = getPositionByItemId(itemId);
        if (position >= 0) {
            oldModel = listModel.remove(position);
            return true;
        }
        return false;
    }

    /**
     * Verifica si existe un item con el {@code itemId} especificado.
     *
     * @param itemId Id a buscar
     * @return {@code true} si existe un item con ese ID, {@code false} en caso contrario
     * @see Model#getItemId()
     */
    public boolean containsItemId(long itemId) {
        return getPositionByItemId(itemId) >= 0;
    }

    /**
     * Obtiene el último modelo removido mediante {@link #removeItem(int)}.
     * <p>
     * Este método retorna el modelo almacenado temporalmente después
     * de una llamada a {@link #removeItem(int)}. Útil si necesitas
     * recuperar información del elemento eliminado.
     *
     * @return El último modelo removido, o {@code null} si no se ha
     *         removido ningún item por posición
     */
    public Model getOldItem() {
        return oldModel;
    }

    /**
     * Actualiza el item en la posición especificada.
     * <p>
     * Reemplaza el modelo existente en la posición con el nuevo modelo.
     * La lista se redibuja automáticamente para reflejar los cambios.
     *
     * @param index Posición del item a actualizar (0-based)
     * @param model Nuevo modelo que reemplazará al existente
     * @throws ArrayIndexOutOfBoundsException si el índice es inválido
     */
    public void updateItem(int index, Model model) {
        listModel.set(index, model);
    }

    /**
     * Remueve todos los items de la lista.
     * <p>
     * La lista queda vacía después de esta operación.
     * Notifica a todos los listeners registrados del cambio.
     */
    public void clearItems() {
        listModel.clear();
    }

    /**
     * Obtiene el modelo en la posición especificada con casting de tipo.
     * <p>
     * Este método realiza un casting seguro del modelo al tipo especificado.
     * Útil cuando se trabaja con subclases específicas de {@link Model}.
     *
     * @param position Posición del item (0-based)
     * @param clazz Clase del tipo esperado para casting seguro
     * @param <M> Tipo genérico que extiende {@link Model}
     * @return Modelo casteado al tipo especificado
     * @throws ArrayIndexOutOfBoundsException si la posición es inválida
     * @throws ClassCastException si el modelo no puede ser casteado al tipo especificado
     */
    public <M extends Model> M getItem(int position, Class<M> clazz) {
        Model model = listModel.get(position);
        return clazz.cast(model);
    }

    /**
     * Obtiene el modelo en la posición especificada.
     *
     * @param index Posición del item (0-based)
     * @return Modelo en la posición especificada
     * @throws ArrayIndexOutOfBoundsException si el índice es inválido
     */
    public Model getItem(int index) {
        return listModel.get(index);
    }

    /**
     * Obtiene una copia de todos los modelos de la lista.
     * <p>
     * Retorna una nueva lista {@link ArrayList} que contiene todos los modelos
     * en el orden actual. Modificar la lista retornada no afecta a la lista interna.
     *
     * @return Lista con todos los modelos, nunca null (puede estar vacía)
     */
    public List<Model> getItems() {
        List<Model> models = new ArrayList<>();
        int size = listModel.size();
        for (int i = 0; i < size; i++) {
            models.add(listModel.get(i));
        }
        return models;
    }

    /**
     * Busca la posición de un modelo comenzando desde una posición específica.
     *
     * @param model Modelo a buscar
     * @param position Posición inicial de búsqueda (0-based)
     * @return Posición del modelo encontrado, o -1 si no se encuentra
     */
    public int getItemPosition(Model model, int position) {
        return listModel.indexOf(model, position);
    }

    /**
     * Obtiene la posición de la primera ocurrencia del modelo especificado.
     *
     * @param model Modelo a buscar
     * @return Posición del modelo (0-based), o -1 si no existe en la lista
     */
    public int getItemPosition(Model model) {
        return listModel.indexOf(model);
    }

    /**
     * Verifica si el modelo especificado existe en la lista.
     *
     * @param model Modelo a verificar
     * @return {@code true} si el modelo está presente, {@code false} en caso contrario
     */
    public boolean containsItem(Model model) {
        return listModel.contains(model);
    }

    /**
     * Verifica si la lista está vacía.
     *
     * @return {@code true} si no hay items, {@code false} si contiene al menos un item
     */
    public boolean isEmpty() {
        return listModel.isEmpty();
    }

    /**
     * Obtiene la cantidad de items en la lista.
     *
     * @return Número de items actualmente en la lista (0 si está vacía)
     */
    public int getItemCount() {
        return listModel.getSize();
    }

    /**
     * Registra un listener para recibir notificaciones de cambios en los datos.
     * <p>
     * El listener será notificado cuando se agreguen, remuevan o modifiquen
     * items en la lista.
     *
     * @param listener Listener a registrar, no puede ser null
     * @see ListDataListener
     * @see #removeListDataListener(ListDataListener)
     */
    public void addListDataListener(ListDataListener listener) {
        listModel.addListDataListener(listener);
    }

    /**
     * Elimina un listener previamente registrado.
     * <p>
     * El listener dejará de recibir notificaciones de cambios.
     *
     * @param listener Listener a eliminar, no puede ser null
     * @see #addListDataListener(ListDataListener)
     */
    public void removeListDataListener(ListDataListener listener) {
        listModel.removeListDataListener(listener);
    }

    /**
     * Crea un {@link JScrollPane} que contiene este EasyList.
     * <p>
     * El scroll pane se configura con:
     * <ul>
     *   <li>Scroll vertical: {@code AS_NEEDED} (aparece cuando es necesario)</li>
     *   <li>Scroll horizontal: {@code NEVER} (no se permite scroll horizontal)</li>
     * </ul>
     * <p>
     * Este método es un utilitario conveniente para agregar la lista a un layout.
     *
     * @return {@link JScrollPane} configurado y listo para agregar al layout
     */
    public JScrollPane createScrollPane() {
        JScrollPane scrollPane = new JScrollPane(this);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scrollPane;
    }
}
