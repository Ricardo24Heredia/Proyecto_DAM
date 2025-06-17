package sv.edu.ues.dam135.appcitas.ui.expediente;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

import sv.edu.ues.dam135.appcitas.R;
import sv.edu.ues.dam135.appcitas.data.AppDatabase;
import sv.edu.ues.dam135.appcitas.data.Cita;
import sv.edu.ues.dam135.appcitas.data.Paciente;
import sv.edu.ues.dam135.appcitas.ui.citas.adapter.CitaLecturaAdapter;
import sv.edu.ues.dam135.appcitas.ui.pacientes.adapter.PacienteLecturaAdapter;

public class ExpedientePacienteFragment extends Fragment {

    private AppDatabase db;
    private EditText edtBuscarDui;
    private Button btnBuscar;
    private RecyclerView recyclerPaciente, recyclerCitas;
    private TextView txtTituloExpediente;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_expediente_paciente, container, false);

        db = AppDatabase.getInstance(getContext());

        edtBuscarDui = root.findViewById(R.id.edtBuscarDui);
        btnBuscar = root.findViewById(R.id.btnBuscarExpediente);
        recyclerPaciente = root.findViewById(R.id.recyclerPacienteLectura);
        recyclerCitas = root.findViewById(R.id.recyclerCitasPaciente);
        txtTituloExpediente = root.findViewById(R.id.txtTituloExpediente);

        recyclerPaciente.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCitas.setLayoutManager(new LinearLayoutManager(getContext()));

        btnBuscar.setOnClickListener(v -> {
            String dui = edtBuscarDui.getText().toString().trim();
            if (!dui.isEmpty()) {
                buscarExpediente(dui);
            } else {
                Toast.makeText(getContext(), "Ingrese un DUI válido", Toast.LENGTH_SHORT).show();
            }
        });

        return root;
    }

    private void buscarExpediente(String dui) {
        Executors.newSingleThreadExecutor().execute(() -> {
            Paciente paciente = db.pacienteDao().obtenerPorDui(dui);
            List<Cita> citas = db.citaDao().obtenerCitasPorDui(dui);

            requireActivity().runOnUiThread(() -> {
                if (paciente != null) {
                    txtTituloExpediente.setText("Expediente de: " + paciente.nombre);

                    PacienteLecturaAdapter pacienteAdapter = new PacienteLecturaAdapter(Collections.singletonList(paciente));
                    recyclerPaciente.setAdapter(pacienteAdapter);

                    CitaLecturaAdapter citaAdapter = new CitaLecturaAdapter(citas);
                    recyclerCitas.setAdapter(citaAdapter);
                } else {
                    txtTituloExpediente.setText("Paciente no encontrado");
                    recyclerPaciente.setAdapter(null);
                    recyclerCitas.setAdapter(null);
                    Toast.makeText(getContext(), "No se encontró un paciente con ese DUI", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
