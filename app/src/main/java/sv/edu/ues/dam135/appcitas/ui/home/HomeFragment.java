package sv.edu.ues.dam135.appcitas.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import sv.edu.ues.dam135.appcitas.R;
import sv.edu.ues.dam135.appcitas.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.txtBienvenida.setText("¡Bienvenido/a a la aplicación movil de citas médicas!");
        binding.txtDescripcion.setText("Aquí puedes administrar citas, ver doctores disponibles y más.");
        binding.imgBienvenida.setImageResource(R.drawable.ic_clinica);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}