# TuAmigoFiel - Gestión de Productos para Mascotas 🚀

## Descripción del Proyecto
**TuAmigoFiel** es una aplicación móvil diseñada para gestionar productos relacionados con mascotas. Permite a los usuarios agregar, editar, eliminar y listar productos de manera eficiente. La aplicación utiliza **Firebase Realtime Database** para almacenar los datos de los productos y **MQTT** para notificaciones en tiempo real, asegurando una experiencia fluida y actualizada para los usuarios.

## Funciones Principales

### Inicio de Sesión y Registro:
- Los usuarios pueden registrarse y acceder a la aplicación mediante una interfaz intuitiva.
- Validación de credenciales para garantizar la seguridad.

### Gestión de Productos:
- **Agregar productos**: Los usuarios pueden añadir productos con detalles como nombre, marca, precio, categoría y descripción.
- **Editar productos**: Modifica los detalles de un producto existente.
- **Eliminar productos**: Elimina productos de la base de datos.
- **Listar productos**: Visualiza todos los productos almacenados en Firebase.

### Notificaciones en Tiempo Real:
- Utiliza **MQTT** para enviar notificaciones en tiempo real cuando se agrega, edita o elimina un producto.

### Conexión con Firebase:
- Los datos de los productos se almacenan en **Firebase Realtime Database**, lo que permite sincronización en tiempo real entre dispositivos.

## Tecnologías Utilizadas
- **Lenguaje**: Java
- **Base de Datos**: Firebase Realtime Database
- **Mensajería en Tiempo Real**: MQTT (Broker público: HiveMQ)
- **Interfaz de Usuario**: Material Design
- **IDE**: Android Studio

## Evidencia Visual

### Pantallas de la Aplicación
- **Pantalla de Inicio de Sesión**:
 ![Pantalla de Inicio de Sesión](https://github.com/ValentinMxC0de/TuAmigoFielApp/blob/main/iniciar_secion.png)

- **Pantalla de Registro**:
  

- **Pantalla Principal**:
  

- **Gestión de Productos**:
  - **Agregar Producto**:
    
  - **Editar Producto**:
    
  - **Eliminar Producto**:
    
  - **Listado de Productos**:
    

## Conexión con Firebase

La aplicación utiliza **Firebase Realtime Database** para almacenar y sincronizar los datos de los productos.

### Estructura de la Base de Datos:
```json
{
  "productos": {
    "-ODEeo0_OYFyMddEox_Y": {
      "categoria": "Alimentos",
      "descripcion": "Alimento Nutritivo para felinos",
      "id": "-ODEeo0_OYFyMddEox_Y",
      "marca": "MasterGato",
      "nombre_producto": "Comida de gato",
      "precio": 10000
    },
    "-ODF0sOtOxwv8RIg5BZX": {
      "categoria": "Alimentos",
      "descripcion": "Alimento nutritivo para canes",
      "id": "-ODF0sOtOxwv8RIg5BZX",
      "marca": "MasterPerro",
      "nombre_producto": "Comida de Perro",
      "precio": 10000
    }
  }
}
```

### Código de Conexión:
La conexión a Firebase se realiza mediante el siguiente código en `MainActivity`:
```java
databaseReference = FirebaseDatabase.getInstance().getReference("productos");
```

## Conexión con MQTT

La aplicación utiliza **MQTT** para enviar notificaciones en tiempo real cuando se realizan cambios en los productos.

### Configuración del Broker MQTT:
- Se utiliza el broker público de HiveMQ: `tcp://broker.hivemq.com:1883`.

### Publicación de Mensajes:
Cuando se agrega, edita o elimina un producto, se publica un mensaje en el tópico `tienda/productos`:
```java
mqttHandler.publish("tienda/productos", mensaje);
```

### Suscripción a Mensajes:
La actividad `Leer` se suscribe al tópico `tienda/productos` para recibir notificaciones en tiempo real:
```java
mqttHandler.subscribe("tienda/productos");
```

### Ejemplo de Mensajes MQTT:


## Cómo Ejecutar el Proyecto

### Clonar el Repositorio:
```bash
git clone https://github.com/tu-usuario/tu-repositorio.git
```

### Abrir en Android Studio:
1. Abre el proyecto en Android Studio.
2. Asegúrate de tener configurado el archivo `google-services.json` para Firebase.

### Configurar Dependencias:
Asegúrate de que las siguientes dependencias estén en tu archivo `build.gradle`:
```gradle
implementation 'com.google.firebase:firebase-database:20.0.5'
implementation 'org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.2.5'
implementation 'org.eclipse.paho:org.eclipse.paho.android.service:1.1.1'
```

### Ejecutar la Aplicación:
1. Conecta un dispositivo físico o utiliza un emulador.
2. Ejecuta la aplicación desde Android Studio.

## Licencia
Este proyecto está bajo la Licencia MIT. Consulta el archivo `LICENSE` para más detalles.
