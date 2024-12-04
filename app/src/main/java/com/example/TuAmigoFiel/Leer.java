package com.example.TuAmigoFiel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Leer extends AppCompatActivity {

    private ListView lst1;
    private ArrayList<String> arreglo = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseReference databaseReference;

    // Variables para MQTT
    private MqttHandler mqttHandler;
    private static final String BROKER_URL = "tcp://broker.hivemq.com:1883";
    private static final String CLIENT_ID = "AndroidClientLeer";
    private static final String TOPIC = "tienda/productos"; // Tópico para notificaciones de productos

    private ArrayList<ProductoMascota> listaProductos = new ArrayList<>(); // Lista para almacenar los productos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        lst1 = findViewById(R.id.lst1);
        databaseReference = FirebaseDatabase.getInstance().getReference("productos");

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arreglo);
        lst1.setAdapter(arrayAdapter);

        // Inicializar MQTT
        mqttHandler = new MqttHandler();
        mqttHandler.connect(BROKER_URL, CLIENT_ID);

        // Configurar el listener para recibir mensajes MQTT
        mqttHandler.setMessageListener((topic, message) -> {
            if (topic.equals(TOPIC)) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Notificación: " + message, Toast.LENGTH_SHORT).show();
                    cargarProductos(); // Recargar la lista de productos
                });
            }
        });

        // Suscribirse al tópico MQTT
        mqttHandler.subscribe(TOPIC);

        // Cargar productos desde Firebase
        cargarProductos();

        // Configurar clic en los elementos de la lista
        lst1.setOnItemClickListener((adapterView, view, position, l) -> {
            // Obtener el producto seleccionado de la lista
            ProductoMascota productoSeleccionado = listaProductos.get(position);

            // Crear un Intent para abrir la actividad Editar
            Intent intent = new Intent(Leer.this, Editar.class);
            intent.putExtra("id", productoSeleccionado.id);
            intent.putExtra("nombre_producto", productoSeleccionado.nombre_producto);
            intent.putExtra("marca", productoSeleccionado.marca);
            intent.putExtra("precio", String.valueOf(productoSeleccionado.precio));
            intent.putExtra("categoria", productoSeleccionado.categoria);
            intent.putExtra("descripcion", productoSeleccionado.descripcion);

            // Iniciar la actividad Editar
            startActivity(intent);
        });
    }

    private void cargarProductos() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                arreglo.clear();
                listaProductos.clear(); // Limpiar la lista de productos

                for (DataSnapshot data : snapshot.getChildren()) {
                    ProductoMascota producto = data.getValue(ProductoMascota.class);
                    if (producto != null) {
                        // Agregar el producto a la lista de productos
                        listaProductos.add(producto);

                        // Agregar el producto al arreglo para mostrarlo en el ListView
                        arreglo.add(producto.id + " - " + producto.nombre_producto + " - " + producto.marca + " - $" + producto.precio);
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(Leer.this, "Error al cargar los datos", Toast.LENGTH_SHORT).show();
            }
        });
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
