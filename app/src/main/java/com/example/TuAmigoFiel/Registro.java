package com.example.TuAmigoFiel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Registro extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnRegister, btnBackToLogin; // Agregar el botón de volver al login
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Inicializar FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Vincular los elementos de la interfaz
        etEmail = findViewById(R.id.et_register_email);
        etPassword = findViewById(R.id.et_register_password);
        btnRegister = findViewById(R.id.btn_register);
        btnBackToLogin = findViewById(R.id.btn_back_to_login); // Vincular el botón de volver al login

        // Configurar el botón de registro
        btnRegister.setOnClickListener(view -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(Registro.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(Registro.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            // Registrar al usuario con Firebase
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Registro exitoso
                            Toast.makeText(Registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();

                            // Redirigir al login
                            Intent intent = new Intent(Registro.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Error en el registro
                            Toast.makeText(Registro.this, "Error: " + Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Configurar el botón de volver al inicio de sesión
        btnBackToLogin.setOnClickListener(view -> {
            // Redirigir al login
            Intent intent = new Intent(Registro.this, Login.class);
            startActivity(intent);
            finish(); // Finalizar la actividad actual
        });
    }
}