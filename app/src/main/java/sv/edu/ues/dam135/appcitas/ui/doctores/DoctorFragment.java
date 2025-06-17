package sv.edu.ues.dam135.appcitas.ui.doctores;

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
import sv.edu.ues.dam135.appcitas.data.Doctor;
import sv.edu.ues.dam135.appcitas.ui.doctores.adapter.DoctorAdapter;

public class DoctorFragment extends Fragment {

    private RecyclerView recyclerView;
    private DoctorAdapter adapter;
    private AppDatabase db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_doctores, container, false);

        recyclerView = root.findViewById(R.id.recyclerDoctores);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = AppDatabase.getInstance(getContext());

        cargarDoctores();

        FloatingActionButton fab = root.findViewById(R.id.fabAgregarDoctor);
        fab.setOnClickListener(v -> {
            DoctorFormDialog dialog = new DoctorFormDialog(getContext(), db, this::cargarDoctores);
            dialog.show();
        });

        return root;
    }

    private void cargarDoctores() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Doctor> doctores = db.doctorDao().obtenerTodos();

            requireActivity().runOnUiThread(() -> {
                if (adapter == null) {
                    adapter = new DoctorAdapter(doctores, db, getContext(), this::cargarDoctores);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.setLista(doctores);
                }
            });
        });
    }
}
