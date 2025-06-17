package sv.edu.ues.dam135.appcitas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.Executors;

import sv.edu.ues.dam135.appcitas.data.AppDatabase;
import sv.edu.ues.dam135.appcitas.data.Paciente;
import sv.edu.ues.dam135.appcitas.data.Usuario;

public class LoginActivity extends AppCompatActivity {

    EditText txtCorreo, txtContrasena;
    Button btnLogin;
    TextView btnIrRegistro;
    AppDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtCorreo = findViewById(R.id.txtCorreo);
        txtContrasena = findViewById(R.id.txtContrasena);
        btnLogin = findViewById(R.id.btnLogin);
        btnIrRegistro = findViewById(R.id.btnIrRegistro);

        db = AppDatabase.getInstance(this);

        btnIrRegistro.setOnClickListener(v -> {
            startActivity(new Intent(this, RegistroActivity.class));
        });

        btnLogin.setOnClickListener(v -> {
            String correo = txtCorreo.getText().toString();
            String contrasena = txtContrasena.getText().toString();

            Executors.newSingleThreadExecutor().execute(() -> {
                Usuario usuario = db.usuarioDao().login(correo, contrasena);

                runOnUiThread(() -> {
                    if (usuario != null) {
                        guardarSesion(usuario);
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
    }

    private void guardarSesion(Usuario usuario) {
        SharedPreferences prefs = getSharedPreferences("sesion", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("nombre", usuario.nombre);
        editor.putString("correo", usuario.correo);
        editor.apply();
    }

}