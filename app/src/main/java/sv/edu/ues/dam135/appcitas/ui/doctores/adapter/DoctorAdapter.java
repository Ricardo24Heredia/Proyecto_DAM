package sv.edu.ues.dam135.appcitas.ui.doctores.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import sv.edu.ues.dam135.appcitas.R;
import sv.edu.ues.dam135.appcitas.data.AppDatabase;
import sv.edu.ues.dam135.appcitas.data.Doctor;
import sv.edu.ues.dam135.appcitas.ui.doctores.DoctorFormDialog;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {

    private List<Doctor> lista;
    private AppDatabase db;
    private Context context;
    private Runnable onChangeCallback;

    public DoctorAdapter(List<Doctor> lista, AppDatabase db, Context context, Runnable onChangeCallback) {
        this.lista = lista;
        this.db = db;
        this.context = context;
        this.onChangeCallback = onChangeCallback;
    }

    public void setLista(List<Doctor> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_doctor, parent, false);
        return new DoctorViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        Doctor doctor = lista.get(position);

        holder.txtNombre.setText(" Nombre: " +doctor.nombre);
        holder.txtDUI.setText(" DUI: " + doctor.dui);
        holder.txtTelefono.setText(" Tel: " + doctor.telefono);
        holder.txtEdad.setText(" Edad: " + doctor.edad +" años");
        holder.txtEspecialidad.setText(" Especialidad: " + doctor.especialidad );
        holder.txtDireccion.setText(" Dirección: " + doctor.direccion );
        holder.btnEditar.setOnClickListener(v -> {
            DoctorFormDialog dialog = new DoctorFormDialog(context, db, onChangeCallback, doctor);
            dialog.show();
        });

        holder.btnEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(context, R.style.MyAlertDialogStyle)
                    .setTitle("Eliminar")
                    .setMessage("¿Deseas eliminar este doctor?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        db.doctorDao().eliminar(doctor);
                        onChangeCallback.run();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtDUI, txtTelefono,txtEdad ,txtEspecialidad, txtDireccion ;
        ImageButton btnEditar, btnEliminar;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombreDoctor);
            txtDUI = itemView.findViewById(R.id.txtDUIDoctor);
            txtTelefono = itemView.findViewById(R.id.txtTelefonoDoctor);
            txtEdad = itemView.findViewById(R.id.txtEdadDoctor);
            txtEspecialidad= itemView.findViewById(R.id.txtEspecialidadDoctor);
            txtDireccion= itemView.findViewById(R.id.txtDireccionDoctor);
            btnEditar = itemView.findViewById(R.id.btnEditarDoctor);
            btnEliminar = itemView.findViewById(R.id.btnEliminarDoctor);
        }
    }
}
