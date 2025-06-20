package sv.edu.ues.dam135.appcitas.ui.pacientes.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executors;

import sv.edu.ues.dam135.appcitas.R;
import sv.edu.ues.dam135.appcitas.data.AppDatabase;
import sv.edu.ues.dam135.appcitas.data.Paciente;
import sv.edu.ues.dam135.appcitas.ui.pacientes.PacienteFormDialog;

public class PacienteAdapter extends RecyclerView.Adapter<PacienteAdapter.PacienteViewHolder> {

    private List<Paciente> lista;
    private AppDatabase db;
    private Context context;
    private Runnable onChangeCallback;

    public PacienteAdapter(List<Paciente> lista, AppDatabase db, Context context, Runnable onChangeCallback) {
        this.lista = lista;
        this.db = db;
        this.context = context;
        this.onChangeCallback = onChangeCallback;
    }

    public void setLista(List<Paciente> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PacienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paciente, parent, false);
        return new PacienteViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull PacienteViewHolder holder, int position) {
        Paciente paciente = lista.get(position);

        holder.txtNombre.setText(" Nombre: " +paciente.nombre);
        holder.txtDUI.setText(" DUI: " + paciente.dui);
        holder.txtTelefono.setText(" Tel: " + paciente.telefono);
        holder.txtEdad.setText(" Edad: " + paciente.edad +" años");
        holder.txtGenero.setText(" Genero: " + paciente.genero );
        holder.txtDireccion.setText(" Dirección: " + paciente.direccion );
        holder.btnEditar.setOnClickListener(v -> {
            PacienteFormDialog dialog = new PacienteFormDialog(context, db, onChangeCallback, paciente);
            dialog.show();
        });

        holder.btnEliminar.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext(), R.style.MyAlertDialogStyle)
                    .setTitle("Eliminar")
                    .setMessage("¿Deseas eliminar este paciente?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            int citasAsociadas = db.citaDao().contarCitasPorDuiPaciente(paciente.dui);
                            if (citasAsociadas > 0) {
                                holder.itemView.post(() ->
                                        Toast.makeText(v.getContext(), "No se puede eliminar, tiene citas registradas.", Toast.LENGTH_LONG).show()
                                );
                            } else {
                                db.pacienteDao().eliminar(paciente);
                                holder.itemView.post(onChangeCallback);
                            }
                        });
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class PacienteViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtDUI, txtTelefono,txtEdad ,txtGenero, txtDireccion ;
        ImageButton btnEditar, btnEliminar;

        public PacienteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombrePaciente);
            txtDUI = itemView.findViewById(R.id.txtDUIPaciente);
            txtTelefono = itemView.findViewById(R.id.txtTelefonoPaciente);
            txtEdad = itemView.findViewById(R.id.txtEdadPaciente);
            txtGenero= itemView.findViewById(R.id.txtGeneroPaciente);
            txtDireccion= itemView.findViewById(R.id.txtDireccionPaciente);
            btnEditar = itemView.findViewById(R.id.btnEditarPaciente);
            btnEliminar = itemView.findViewById(R.id.btnEliminarPaciente);
        }
    }
}