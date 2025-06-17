package sv.edu.ues.dam135.appcitas.ui.doctores;


import android.app.Dialog;
import android.content.Context;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import sv.edu.ues.dam135.appcitas.R;
import sv.edu.ues.dam135.appcitas.data.AppDatabase;
import sv.edu.ues.dam135.appcitas.data.Doctor;


public class DoctorFormDialog {
    private final Context context;
    private final AppDatabase db;
    private final Runnable onChangeCallback;
    private final Doctor doctorExistente;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public DoctorFormDialog(Context context, AppDatabase db, Runnable onChangeCallback) {
        this(context, db, onChangeCallback, null);
    }

    public DoctorFormDialog(Context context, AppDatabase db, Runnable onChangeCallback, Doctor doctorExistente) {
        this.context = context;
        this.db = db;
        this.onChangeCallback = onChangeCallback;
        this.doctorExistente = doctorExistente;
    }

    public void show() {
        Dialog dialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_doctor, null);
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
        Spinner spEspecialidad = view.findViewById(R.id.spEspecialidad);
        EditText etTelefono = view.findViewById(R.id.etTelefono);
        EditText etDireccion = view.findViewById(R.id.etDireccion);
        Button btnGuardar = view.findViewById(R.id.btnGuardarDoctor);
        Button btnCancelar = view.findViewById(R.id.btnCancelarDoctor);

        ArrayAdapter<CharSequence> adapterEspecialidad = ArrayAdapter.createFromResource(
                context,
                R.array.especialidad_array,
                android.R.layout.simple_spinner_item
        );
        adapterEspecialidad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEspecialidad.setAdapter(adapterEspecialidad);

        if (doctorExistente != null) {
            etNombre.setText(doctorExistente.nombre);
            etDUI.setText(doctorExistente.dui);
            etEdad.setText(String.valueOf(doctorExistente.edad));
            etTelefono.setText(doctorExistente.telefono);
            etDireccion.setText(doctorExistente.direccion);
            int pos = adapterEspecialidad.getPosition(doctorExistente.especialidad);
            spEspecialidad.setSelection(pos);
        }

        btnCancelar.setOnClickListener(v -> dialog.dismiss());

        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String dui = etDUI.getText().toString().trim();
            String edadStr = etEdad.getText().toString().trim();
            String especialidad = spEspecialidad.getSelectedItem().toString();
            String telefono = etTelefono.getText().toString().trim();
            String direccion = etDireccion.getText().toString().trim();

            if (nombre.isEmpty() || dui.isEmpty() || edadStr.isEmpty() ||
                    especialidad.equals("Seleccione especialidad") || telefono.isEmpty() || direccion.isEmpty()) {
                Toast.makeText(context, "Por favor complete todos los campos correctamente.", Toast.LENGTH_SHORT).show();
                return;
            }

            int edad;
            try {
                edad = Integer.parseInt(edadStr);
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Edad inválida.", Toast.LENGTH_SHORT).show();
                return;
            }

            executor.execute(() -> {
                if (doctorExistente == null) {
                    Doctor nuevo = new Doctor();
                    nuevo.nombre = nombre;
                    nuevo.dui = dui;
                    nuevo.edad = edad;
                    nuevo.especialidad = especialidad;
                    nuevo.telefono = telefono;
                    nuevo.direccion = direccion;
                    db.doctorDao().insertar(nuevo);
                } else {
                    doctorExistente.nombre = nombre;
                    doctorExistente.dui = dui;
                    doctorExistente.edad = edad;
                    doctorExistente.especialidad = especialidad;
                    doctorExistente.telefono = telefono;
                    doctorExistente.direccion = direccion;
                    db.doctorDao().actualizar(doctorExistente);
                }

                ((android.app.Activity) context).runOnUiThread(() -> {
                    onChangeCallback.run();
                    dialog.dismiss();
                });
            });
        });
    }
}

