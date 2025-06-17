package sv.edu.ues.dam135.appcitas.ui.citas;

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
import sv.edu.ues.dam135.appcitas.data.Cita;
import sv.edu.ues.dam135.appcitas.ui.citas.adapter.CitaAdapter;

public class CitaFragment extends Fragment {

    private RecyclerView recyclerView;
    private CitaAdapter adapter;
    private AppDatabase db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_citas, container, false);
        recyclerView = root.findViewById(R.id.recyclerCitas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        db = AppDatabase.getInstance(getContext());
        cargarCitas();

        FloatingActionButton fab = root.findViewById(R.id.fabAgregarCita);
        fab.setOnClickListener(v -> {
            CitaFormDialog dialog = new CitaFormDialog(getContext(), db, this::cargarCitas, null);
            dialog.show();
        });

        return root;
    }

    private void cargarCitas() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Cita> citas = db.citaDao().obtenerTodas();

            requireActivity().runOnUiThread(() -> {
                if (adapter == null) {
                    adapter = new CitaAdapter(citas, db, getContext(), this::cargarCitas);
                    recyclerView.setAdapter(adapter);
                } else {
                    adapter.setLista(citas);
                }
            });
        });
    }
}
