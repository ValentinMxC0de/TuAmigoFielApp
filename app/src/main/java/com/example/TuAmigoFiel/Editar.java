package com.example.TuAmigoFiel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Editar extends AppCompatActivity {

    private EditText ed_nombreProducto, ed_marca, ed_precio, ed_categoria, ed_descripcion;
    private Button b_editar, b_eliminar, b_volver;
    private DatabaseReference databaseReference;
    private String id;

    // Variables para MQTT
    private MqttHandler mqttHandler;
    private static final String BROKER_URL = "tcp://broker.hivemq.com:1883";
    private static final String CLIENT_ID = "AndroidClientEditar";
    private static final String TOPIC = "tienda/productos"; // Tópico para notificaciones de productos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Inicializar vistas
        ed_nombreProducto = findViewById(R.id.et_nombreProducto);
        ed_marca = findViewById(R.id.et_marca);
        ed_precio = findViewById(R.id.et_precio);
        ed_categoria = findViewById(R.id.et_categoria);
        ed_descripcion = findViewById(R.id.et_descripcion);
        b_editar = findViewById(R.id.btn_editar);
        b_eliminar = findViewById(R.id.btn_eliminar);
        b_volver = findViewById(R.id.btn_volver);

        // Inicializar Firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference("productos");

        // Inicializar MQTT
        mqttHandler = new MqttHandler();
        mqttHandler.connect(BROKER_URL, CLIENT_ID);

        // Obtener datos del Intent
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        ed_nombreProducto.setText(intent.getStringExtra("nombre_producto"));
        ed_marca.setText(intent.getStringExtra("marca"));
        ed_precio.setText(intent.getStringExtra("precio"));
        ed_categoria.setText(intent.getStringExtra("categoria"));
        ed_descripcion.setText(intent.getStringExtra("descripcion"));

        // Configurar botones
        b_editar.setOnClickListener(v -> editar());
        b_eliminar.setOnClickListener(v -> eliminar());
        b_volver.setOnClickListener(v -> volver());
    }

    public void editar() {
        String nombreProducto = ed_nombreProducto.getText().toString();
        String marca = ed_marca.getText().toString();
        String precio = ed_precio.getText().toString();
        String categoria = ed_categoria.getText().toString();
        String descripcion = ed_descripcion.getText().toString();

        if (nombreProducto.isEmpty() || marca.isEmpty() || precio.isEmpty() || categoria.isEmpty() || descripcion.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un objeto ProductoMascota
        ProductoMascota producto = new ProductoMascota(id, nombreProducto, marca, Double.parseDouble(precio), categoria, descripcion);

        // Actualizar en Firebase
        databaseReference.child(id).setValue(producto)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Producto actualizado correctamente", Toast.LENGTH_SHORT).show();

                    // Publicar mensaje en MQTT
                    if (mqttHandler != null) {
                        String mensaje = "Producto actualizado: " + nombreProducto + ", Marca: " + marca + ", Precio: " + precio;
                        mqttHandler.publish(TOPIC, mensaje);
                    }

                    // Regresar a la lista de productos
                    volver();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al actualizar el producto", Toast.LENGTH_SHORT).show());
    }

    public void eliminar() {
        // Eliminar de Firebase
        databaseReference.child(id).removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();

                    // Publicar mensaje en MQTT
                    if (mqttHandler != null) {
                        String mensaje = "Producto eliminado: ID " + id;
                        mqttHandler.publish(TOPIC, mensaje);
                    }

                    // Regresar a la lista de productos
                    volver();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al eliminar el producto", Toast.LENGTH_SHORT).show());
    }

    private void volver() {
        Intent intent = new Intent(Editar.this, Leer.class);
        startActivity(intent);
        finish();
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