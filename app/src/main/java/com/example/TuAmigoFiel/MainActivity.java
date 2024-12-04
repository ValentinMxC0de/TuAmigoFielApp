package com.example.TuAmigoFiel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText ed_nombreProducto, ed_marca, ed_precio, ed_categoria, ed_descripcion;
    private Button b_agregar;
    private DatabaseReference databaseReference;

    // Variables para MQTT
    private MqttHandler mqttHandler;
    private static final String BROKER_URL = "tcp://broker.hivemq.com:1883";
    private static final String CLIENT_ID = "AndroidClient";
    private static final String TOPIC = "tienda/productos"; // Tópico para notificaciones de productos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        ed_nombreProducto = findViewById(R.id.et_nombreProducto);
        ed_marca = findViewById(R.id.et_marca);
        ed_precio = findViewById(R.id.et_precio);
        ed_categoria = findViewById(R.id.et_categoria);
        ed_descripcion = findViewById(R.id.et_descripcion);
        b_agregar = findViewById(R.id.btn_agregar);

        // Inicializar Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("productos");

        // Inicializar MQTT
        mqttHandler = new MqttHandler();
        mqttHandler.connect(BROKER_URL, CLIENT_ID);

        // Configurar botón para agregar producto
        b_agregar.setOnClickListener(v -> insertar());
    }

    public void insertar() {
        String nombreProducto = ed_nombreProducto.getText().toString();
        String marca = ed_marca.getText().toString();
        String precio = ed_precio.getText().toString();
        String categoria = ed_categoria.getText().toString();
        String descripcion = ed_descripcion.getText().toString();

        if (nombreProducto.isEmpty() || marca.isEmpty() || precio.isEmpty() || categoria.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generar un ID único para el producto
        String id = databaseReference.push().getKey();

        // Crear un objeto ProductoMascota
        ProductoMascota producto = new ProductoMascota(id, nombreProducto, marca, Double.parseDouble(precio), categoria, descripcion);

        // Guardar en Firebase
        databaseReference.child(id).setValue(producto)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Producto agregado correctamente", Toast.LENGTH_SHORT).show();
                    limpiarCampos();

                    // Publicar mensaje en MQTT
                    if (mqttHandler != null) {
                        String mensaje = "Producto agregado: " + nombreProducto + ", Marca: " + marca + ", Precio: " + precio;
                        mqttHandler.publish(TOPIC, mensaje);
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al agregar el producto", Toast.LENGTH_SHORT).show());
    }

    private void limpiarCampos() {
        ed_nombreProducto.setText("");
        ed_marca.setText("");
        ed_precio.setText("");
        ed_categoria.setText("");
        ed_descripcion.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Desconectar MQTT al cerrar la actividad
        if (mqttHandler != null) {
            mqttHandler.disconnect();
        }
    }
}