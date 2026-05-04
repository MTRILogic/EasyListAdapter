# EasyListAdapter

Una librería Java para Swing que implementa el patrón **RecyclerView** de Android en componentes `JList`. Permite crear listas eficientes con múltiples tipos de items, reutilización de componentes y tracking por ID único.

## Características

- **Patrón ViewHolder**: Reutilización automática de componentes visuales por tipo
- **Múltiples tipos de items**: Soporte nativo para diferentes layouts en la misma lista
- **Ids únicos y estables**: Identificación de items independiente de su posición (útil para filtrados y animaciones)
- **API simplificada**: Métodos convenientes para CRUD operaciones por posición o por ID
- **Altura variable**: Cada tipo de item puede tener su propia altura
- **Sin dependencias externas**: Solo requiere `SpringPanel` para el layout (incluido como dependencia)

## Instalación

### JitPack

Agrega el repositorio JitPack en tu `build.gradle` o `settings.gradle`:

```kotlin
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}
```

Agrega la dependencia:

```kotlin
dependencies {
    implementation("com.github.MTRILogic:EasyListAdapter:1.0.0")
}
```

## Uso rápido

### 1. Crear un Model

```java
public class MyModel extends Model {
    private String text;

    public MyModel() {
        super(0); // itemType = 0, itemId generado automáticamente
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
```

### 2. Crear un Item (vista)

```java
public class MyItem extends Item<MyModel> {
    private final JLabel label = new JLabel();

    public MyItem() {
        super(MyModel.class);
        setBackground(Color.WHITE);
        with(label).north(5).west(5).east(5).south(5);
    }

    @Override
    protected void onBindModel() {
        label.setText(model.getText());
        label.setBackground(selected ? Color.BLUE : Color.WHITE);
    }

    @Override
    protected int onNewHeight(int height) {
        return 50; // Altura fija de 50px
    }
}
```

### 3. Crear y usar la lista

```
// Crear EasyList con un ListAdapter.Listener
EasyList list = new EasyList(itemType -> new MyItem());

// Agregar items (Ids únicos generados automáticamente)
list.appendItem(new MyModel());
list.appendItem(new MyModel());

// Crear scroll pane y agregar al panel
JScrollPane scrollPane = list.createScrollPane();
```

## Conceptos clave

### itemType versus itemId

| Propiedad    | Propósito                                               | Ejemplo                          |
|--------------|---------------------------------------------------------|----------------------------------|
| **itemType** | Define qué clase `Item` renderiza el modelo             | `0=HeaderItem`, `1=ContentItem`  |
| **itemId**   | Identifica el dato único independientemente de posición | ID autoincremental: `1, 2, 3...` |

`itemType` determina la **arquitectura visual** (qué componentes Swing se crean). `itemId` permite **tracking estable** durante filtrados, búsquedas o animaciones.

### Generación de Ids

Por defecto, los Ids se generan automáticamente de forma ascendente:

```java
// IDs automáticos: 1, 2, 3...
Model model1 = new MyModel(); // itemId = 1
Model model2 = new MyModel(); // itemId = 2
```

Para control manual (ej.: Ids de base de datos):

```java
// Constructor con ID manual
Model model = new Model(101L, 0) {}; // itemId = 101
```

## API Reference

### EasyList

Operaciones por posición:
- `appendItem(Model)` - Agregar al final
- `insertItem(int, Model)` - Insertar en posición
- `removeItem(int)` / `removeItem(Model)` - Remover por posición o modelo
- `updateItem(int, Model)` - Actualizar en posición
- `getItem(int)` / `getItem(int, Class)` - Obtener por posición
- `getItemCount()` - Cantidad de items

Operaciones por ID (independientes de posición):
- `getPositionByItemId(long)` - Encontrar posición actual de un ID
- `getItemById(long, Class)` - Obtener modelo por ID
- `updateItemById(long, Model)` - Actualizar por ID
- `removeItemById(long)` - Remover por ID
- `containsItemId(long)` - Verificar si existe el ID

Utilidades:
- `clearItems()` - Vaciar lista
- `createScrollPane()` - Crear JScrollPane configurado
- `addListDataListener(ListDataListener)` - Escuchar cambios

### Model

- `getItemId()` - Obtener ID único
- `getItemType()` - Obtener tipo de item
- `resetIdCounter()` - Reiniciar contador de IDs (para tests)

### Item<M extends Model>

Métodos para sobrescribir:
- `onBindModel()` - Actualizar UI cuando cambian los datos
- `onNewHeight(int)` - Definir altura personalizada

Campos accesibles:
- `model` - El modelo enlazado
- `position` - Posición actual en la lista
- `selected` - Si está seleccionado
- `focused` - Si tiene el foco

## Ejemplo completo

Ver el módulo `/demo` para una implementación funcional con:
- Dos tipos de items (`LocalItem`, `RemoteItem`)
- Alturas variables (50px vs 30px)
- 100 items generados dinámicamente

## Requisitos

- Java 8 o superior
- SpringPanel 1.0.0 (incluido automáticamente vía JitPack)

## Licencia

MIT License - Libre para uso personal y comercial.

---

**Autor:** MTRILogic
