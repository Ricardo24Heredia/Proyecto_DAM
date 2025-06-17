package sv.edu.ues.dam135.appcitas.ui.pacientes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.Executors;

import sv.edu.ues.dam135.appcitas.R;
import sv.edu.ues.dam135.appcitas.data.AppDatabase;
import sv.edu.ues.dam135.appcitas.data.Paciente;
import sv.edu.ues.dam135.appcitas.ui.pacientes.adapter.PacienteAdapter;

public class PacienteFragment extends Fragment {
    private RecyclerView recyclerView;
    private PacienteAdapter adapter;
    private AppDatabase db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_pacientes, container, false);
        recyclerView = root.findViewById(R.id.recyclerPacientes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = AppDatabase.getInstance(getContext());

        cargarPacientes();

        FloatingActionButton fab = root.findViewById(R.id.fabAgregarPaciente);
        fab.setOnClickListener(v -> {
            PacienteFormDialog dialog = new PacienteFormDialog(getContext(), db, this::cargarPacientes);
            dialog.show();
        });

        return root;
    }

    private void cargarPacientes() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Paciente> pacientes = db.pacienteDao().obtenerTodos();

            requireActivity().runOnUiThread(() -> {
                if (adapter == null) {
                    adapter = new PacienteAdapter(pacientes, db, getContext(), this::cargarPacientes);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.setLista(pacientes);
                }
            });
        });
    }
}