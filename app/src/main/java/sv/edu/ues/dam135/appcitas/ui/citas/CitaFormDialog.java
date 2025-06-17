package sv.edu.ues.dam135.appcitas.ui.citas;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import sv.edu.ues.dam135.appcitas.R;
import sv.edu.ues.dam135.appcitas.data.AppDatabase;
import sv.edu.ues.dam135.appcitas.data.Cita;
import sv.edu.ues.dam135.appcitas.data.Doctor;
import sv.edu.ues.dam135.appcitas.data.Paciente;

public class CitaFormDialog extends Dialog {
    private Context context;
    private AppDatabase db;
    private Runnable onActualizar;
    private Cita citaEditar;

    private EditText edtDuiPaciente, edtNombrePaciente, edtMotivo, edtFecha, edtHora;
    private Spinner spinnerDoctor, spinnerEstado;
    private TextView txtEspecialidad;

    private ImageButton btnImgFecha, btnImgHora;
    private Button btnGuardar, btnCancelar;

    private String fechaSeleccionada = "";
    private String horaSeleccionada = "";

    private List<Doctor> listaDoctores;
    private ArrayAdapter<String> adapterDoctores;

    public CitaFormDialog(@NonNull Context context, AppDatabase db, Runnable onActualizar, Cita citaEditar) {
        super(context);
        this.context = context;
        this.db = db;
        this.onActualizar = onActualizar;
        this.citaEditar = citaEditar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_cita_form);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        edtDuiPaciente = findViewById(R.id.edtDuiPaciente);
        edtNombrePaciente = findViewById(R.id.edtNombrePaciente);
        edtNombrePaciente.setFocusable(false);
        edtNombrePaciente.setClickable(false);
        spinnerDoctor = findViewById(R.id.spinnerDoctor);
        txtEspecialidad = findViewById(R.id.txtEspecialidadDoctor);
        edtFecha = findViewById(R.id.edtfechaCita);
        edtHora = findViewById(R.id.edthoraCita);
        btnImgFecha = findViewById(R.id.btnimgFecha);
        btnImgHora = findViewById(R.id.btnimgHora);
        edtMotivo = findViewById(R.id.edtMotivo);
        spinnerEstado = findViewById(R.id.spinnerEstado);
        btnGuardar = findViewById(R.id.btnGuardarCita);
        btnCancelar = findViewById(R.id.btnCancelarCita);

        new Thread(() -> {
            listaDoctores = db.doctorDao().obtenerTodos();

            List<String> nombresDoctores = new ArrayList<>();
            for (Doctor d : listaDoctores) {
                nombresDoctores.add(d.nombre);
            }

            ((Activity) context).runOnUiThread(() -> {
                adapterDoctores = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, nombresDoctores);
                adapterDoctores.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerDoctor.setAdapter(adapterDoctores);

                spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                        Doctor doc = listaDoctores.get(pos);
                        txtEspecialidad.setText(doc.especialidad);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        txtEspecialidad.setText("");
                    }
                });

                if (citaEditar != null) {
                    edtDuiPaciente.setText(citaEditar.duiPaciente);
                    edtNombrePaciente.setText(citaEditar.nombrePaciente);

                    int posDoc = 0;
                    for (int i = 0; i < listaDoctores.size(); i++) {
                        if (listaDoctores.get(i).id == citaEditar.idDoctor) {
                            posDoc = i;
                            break;
                        }
                    }
                    spinnerDoctor.setSelection(posDoc);
                    txtEspecialidad.setText(citaEditar.especialidad);

                    fechaSeleccionada = citaEditar.fecha;
                    horaSeleccionada = citaEditar.hora;
                    edtFecha.setText(fechaSeleccionada);
                    edtHora.setText(horaSeleccionada);
                    edtMotivo.setText(citaEditar.motivo);

                    List<String> estados = Arrays.asList("Pendiente", "Completada", "Cancelada");
                    ArrayAdapter<String> adapterEstados = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, estados);
                    adapterEstados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEstado.setAdapter(adapterEstados);

                    int posEstado = estados.indexOf(citaEditar.estado);
                    if (posEstado >= 0) spinnerEstado.setSelection(posEstado);
                }
            });
        }).start();

        List<String> estados = Arrays.asList("Pendiente", "Completada", "Cancelada");
        ArrayAdapter<String> adapterEstados = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, estados);
        adapterEstados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapterEstados);

        edtDuiPaciente.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String dui = s.toString();
                if (!dui.isEmpty()) {
                    new Thread(() -> {
                        Paciente paciente = db.pacienteDao().obtenerPorDui(dui);
                        ((Activity) context).runOnUiThread(() -> {
                            if (paciente != null) {
                                edtNombrePaciente.setText(paciente.nombre);
                            } else {
                                edtNombrePaciente.setText("");
                            }
                        });
                    }).start();
                } else {
                    edtNombrePaciente.setText("");
                }
            }
        });

        btnImgFecha.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int anio = c.get(Calendar.YEAR);
            int mes = c.get(Calendar.MONTH);
            int dia = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(context, (view, year, month, dayOfMonth) -> {
                month += 1;
                fechaSeleccionada = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, dayOfMonth);
                edtFecha.setText(fechaSeleccionada);
            }, anio, mes, dia);
            datePickerDialog.show();
        });

        btnImgHora.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            int hora = c.get(Calendar.HOUR_OF_DAY);
            int minuto = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(context, (view, hourOfDay, minute1) -> {
                horaSeleccionada = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                edtHora.setText(horaSeleccionada);
            }, hora, minuto, true);
            timePickerDialog.show();
        });

        btnGuardar.setOnClickListener(v -> {
            String dui = edtDuiPaciente.getText().toString().trim();
            String nombrePac = edtNombrePaciente.getText().toString().trim();
            int posDoctor = spinnerDoctor.getSelectedItemPosition();
            Doctor doc = listaDoctores.get(posDoctor);
            String especialidad = doc.especialidad;
            String fecha = fechaSeleccionada;
            String hora = horaSeleccionada;
            String motivo = edtMotivo.getText().toString().trim();
            String estado = spinnerEstado.getSelectedItem().toString();

            if (dui.isEmpty() || nombrePac.isEmpty() || fecha.isEmpty() || hora.isEmpty() || motivo.isEmpty()) {
                Toast.makeText(context, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (citaEditar == null) {
                Cita nuevaCita = new Cita();
                nuevaCita.duiPaciente = dui;
                nuevaCita.nombrePaciente = nombrePac;
                nuevaCita.idDoctor = doc.id;
                nuevaCita.nombreDoctor = doc.nombre;
                nuevaCita.especialidad = especialidad;
                nuevaCita.fecha = fecha;
                nuevaCita.hora = hora;
                nuevaCita.motivo = motivo;
                nuevaCita.estado = estado;

                new Thread(() -> {
                    db.citaDao().insertar(nuevaCita);
                    if (onActualizar != null) ((Activity) context).runOnUiThread(onActualizar);
                }).start();
            } else {
                citaEditar.duiPaciente = dui;
                citaEditar.nombrePaciente = nombrePac;
                citaEditar.idDoctor = doc.id;
                citaEditar.nombreDoctor = doc.nombre;
                citaEditar.especialidad = especialidad;
                citaEditar.fecha = fecha;
                citaEditar.hora = hora;
                citaEditar.motivo = motivo;
                citaEditar.estado = estado;

                new Thread(() -> {
                    db.citaDao().actualizar(citaEditar);
                    if (onActualizar != null) ((Activity) context).runOnUiThread(onActualizar);
                }).start();
            }

            dismiss();
        });

        btnCancelar.setOnClickListener(v -> dismiss());
    }
}

