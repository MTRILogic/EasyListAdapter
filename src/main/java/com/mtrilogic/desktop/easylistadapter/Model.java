package com.mtrilogic.desktop.easylistadapter;

/**
 * Clase base para modelos que representan datos en una lista.
 * <p>
 * Cada modelo tiene un {@link #itemId} único que lo identifica independientemente
 * de su posición, y un {@link #itemType} que determina qué tipo de {@link Item}
 * lo renderizará visualmente.
 */
@SuppressWarnings("unused")
public abstract class Model {
    /** Contador estático para generar Ids automáticos de forma ascendente */
    private static long nextId = 1;

    private final long itemId;
    private final int itemType;

    /**
     * Crea un modelo con ID generado automáticamente.
     * <p>
     * El ID se asigna secuencialmente usando un contador interno estático,
     * garantizando unicidad global dentro de la JVM.
     *
     * @param itemType Tipo de item que determina qué {@link Item} lo renderizará
     */
    public Model(int itemType) {
        this.itemId = nextId++;
        this.itemType = itemType;
    }

    /**
     * Crea un modelo con ID especificado manualmente.
     * <p>
     * Útil cuando necesitas controlar el ID (ej: IDs de base de datos).
     * No afecta el contador de IDs automáticos.
     *
     * @param itemId ID único especificado manualmente
     * @param itemType Tipo de item que determina qué {@link Item} lo renderizará
     */
    public Model(long itemId, int itemType) {
        this.itemId = itemId;
        this.itemType = itemType;
    }

    /**
     * Retorna el ID único de este modelo.
     * <p>
     * Este ID identifica el item independientemente de su posición en la lista,
     * permitiendo tracking durante filtrados, ordenamientos o actualizaciones.
     *
     * @return Id único del item
     */
    public long getItemId() {
        return itemId;
    }

    /**
     * Retorna el tipo de item que determina qué clase {@link Item} lo renderiza.
     *
     * @return Tipo de item (ej.: 0, 1, 2, etc.)
     * @see ListAdapter.Listener#getItem(int)
     */
    public int getItemType() {
        return itemType;
    }

    /**
     * Reinicia el contador de Ids automáticos a 1.
     * <p>
     * Útil para tests o cuando necesitas comenzar una nueva secuencia.
     * No afecta los modelos ya creados.
     */
    public static void resetIdCounter() {
        nextId = 1;
    }

    /**
     * Obtiene el próximo ID que se asignará.
     * <p>
     * Útil para predecir el ID o sincronizar con sistemas externos.
     *
     * @return El próximo ID disponible
     */
    public static long getNextId() {
        return nextId;
    }
}
