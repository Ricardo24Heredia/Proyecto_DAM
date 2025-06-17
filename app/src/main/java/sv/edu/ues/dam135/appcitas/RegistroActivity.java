package sv.edu.ues.dam135.appcitas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;

import sv.edu.ues.dam135.appcitas.data.AppDatabase;
import sv.edu.ues.dam135.appcitas.data.Usuario;

public class RegistroActivity  extends AppCompatActivity {

    EditText txtNombre, txtCorreo, txtContrasena;
    Button btnRegistrar,btnRegresar;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtNombre = findViewById(R.id.txtNombre);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtContrasena = findViewById(R.id.txtContrasena);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnRegresar = findViewById(R.id.btnRegresar);

        db = AppDatabase.getInstance(this);

        btnRegistrar.setOnClickListener(v -> {
            String nombre = txtNombre.getText().toString();
            String correo = txtCorreo.getText().toString();
            String contrasena = txtContrasena.getText().toString();

            if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Executors.newSingleThreadExecutor().execute(() -> {
                Usuario existente = db.usuarioDao().obtenerPorCorreo(correo);

                runOnUiThread(() -> {
                    if (existente != null) {
                        Toast.makeText(RegistroActivity.this, "El correo ya está registrado", Toast.LENGTH_SHORT).show();
                    } else {
                        Usuario usuario = new Usuario();
                        usuario.nombre = nombre;
                        usuario.correo = correo;
                        usuario.contrasena = contrasena;

                        Executors.newSingleThreadExecutor().execute(() -> {
                            db.usuarioDao().insertar(usuario);

                            runOnUiThread(() -> {
                                Toast.makeText(RegistroActivity.this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show();
                                finish(); // Regresa al Login
                            });
                        });
                    }
                });
            });
        });

        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
