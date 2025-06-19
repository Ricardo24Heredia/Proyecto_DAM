package sv.edu.ues.dam135.appcitas.ui.pacientes;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sv.edu.ues.dam135.appcitas.R;
import sv.edu.ues.dam135.appcitas.data.AppDatabase;
import sv.edu.ues.dam135.appcitas.data.Paciente;

public class PacienteFormDialog {

    private Context context;
    private AppDatabase db;
    private Runnable onChangeCallback;
    private Paciente pacienteExistente;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    public PacienteFormDialog(Context context, AppDatabase db, Runnable onChangeCallback) {
        this(context, db, onChangeCallback, null);
    }

    public PacienteFormDialog(Context context, AppDatabase db, Runnable onChangeCallback, Paciente pacienteExistente) {
        this.context = context;
        this.db = db;
        this.onChangeCallback = onChangeCallback;
        this.pacienteExistente = pacienteExistente;
    }

    public void show() {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_paciente, null);
        dialog.setContentView(view);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            );
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        }

        dialog.show();

        EditText etNombre = view.findViewById(R.id.etNombre);
        EditText etDUI = view.findViewById(R.id.etDUI);
        EditText etEdad = view.findViewById(R.id.etEdad);
        Spinner spGenero = view.findViewById(R.id.spGenero);
        EditText etTelefono = view.findViewById(R.id.etTelefono);
        EditText etDireccion = view.findViewById(R.id.etDireccion);
        Button btnGuardar = view.findViewById(R.id.btnGuardarPaciente);
        Button btnCancelar = view.findViewById(R.id.btnCancelarPaciente);

        ArrayAdapter<CharSequence> adapterGenero = ArrayAdapter.createFromResource(
                context,
                R.array.genero_array,
                android.R.layout.simple_spinner_item
        );
        adapterGenero.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGenero.setAdapter(adapterGenero);

        if (pacienteExistente != null) {
            etNombre.setText(pacienteExistente.nombre);
            etDUI.setText(pacienteExistente.dui);
            etEdad.setText(String.valueOf(pacienteExistente.edad));
            etTelefono.setText(pacienteExistente.telefono);
            etDireccion.setText(pacienteExistente.direccion);
            int spinnerPosition = adapterGenero.getPosition(pacienteExistente.genero);
            spGenero.setSelection(spinnerPosition);
        }

        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String dui = etDUI.getText().toString().trim();
            String edadTexto = etEdad.getText().toString().trim();
            String genero = spGenero.getSelectedItem().toString();
            String telefono = etTelefono.getText().toString().trim();
            String direccion = etDireccion.getText().toString().trim();

            if (nombre.isEmpty() || dui.isEmpty() || edadTexto.isEmpty() ||
                    genero.equals("Seleccione género") || telefono.isEmpty() || direccion.isEmpty()) {
                Toast.makeText(context, "Por favor complete todos los campos correctamente.", Toast.LENGTH_SHORT).show();
                return;
            }

            int edad;
            try {
                edad = Integer.parseInt(edadTexto);
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Edad inválida.", Toast.LENGTH_SHORT).show();
                return;
            }

            executor.execute(() -> {
                if (pacienteExistente == null) {
                    Paciente nuevo = new Paciente();
                    nuevo.nombre = nombre;
                    nuevo.dui = dui;
                    nuevo.edad = edad;
                    nuevo.genero = genero;
                    nuevo.telefono = telefono;
                    nuevo.direccion = direccion;
                    db.pacienteDao().insertar(nuevo);
                } else {
                    pacienteExistente.nombre = nombre;
                    pacienteExistente.dui = dui;
                    pacienteExistente.edad = edad;
                    pacienteExistente.genero = genero;
                    pacienteExistente.telefono = telefono;
                    pacienteExistente.direccion = direccion;
                    db.pacienteDao().actualizar(pacienteExistente);
                }

                ((android.app.Activity) context).runOnUiThread(() -> {
                    onChangeCallback.run();
                    dialog.dismiss();
                });
            });
        });

        btnCancelar.setOnClickListener(v -> dialog.dismiss());
    }
}

