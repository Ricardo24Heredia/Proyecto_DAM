package sv.edu.ues.dam135.appcitas.ui.citas.adapter;

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
import sv.edu.ues.dam135.appcitas.data.Cita;
import sv.edu.ues.dam135.appcitas.ui.citas.CitaFormDialog;

public class CitaLecturaAdapter extends RecyclerView.Adapter<CitaLecturaAdapter.CitaViewHolder> {

    private List<Cita> lista;

    public CitaLecturaAdapter(List<Cita> lista) {
        this.lista = lista;
    }

    public void setLista(List<Cita> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CitaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cita_lectura, parent, false);
        return new CitaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull CitaViewHolder holder, int position) {
        Cita cita = lista.get(position);

        holder.txtPaciente.setText("Paciente: " + cita.nombrePaciente);
        holder.txtDUI.setText("DUI: " + cita.duiPaciente);
        holder.txtDoctor.setText("Doctor: " + cita.nombreDoctor);
        holder.txtEspecialidad.setText("Especialidad: " + cita.especialidad);
        holder.txtFecha.setText("Fecha de la cita: " + cita.fecha);
        holder.txtHora.setText("Hora de la cita: " + cita.hora);
        holder.txtID.setText("N° Cita: " + cita.id);
        holder.txtMotivo.setText("Motivo: " + cita.motivo);
        holder.txtEstado.setText("Estado: " + cita.estado);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class CitaViewHolder extends RecyclerView.ViewHolder {
        TextView txtPaciente, txtDUI, txtDoctor, txtEspecialidad, txtFecha,
                txtHora, txtID, txtMotivo, txtEstado;

        public CitaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPaciente = itemView.findViewById(R.id.txtNombrePaciente);
            txtDUI = itemView.findViewById(R.id.txtDUIPaciente);
            txtDoctor = itemView.findViewById(R.id.txtNombreDoctor);
            txtEspecialidad = itemView.findViewById(R.id.txtEspecialidadDoctor);
            txtFecha = itemView.findViewById(R.id.txtFechaCita);
            txtHora = itemView.findViewById(R.id.txtHoraCita);
            txtID = itemView.findViewById(R.id.txtNumeroCita);
            txtMotivo = itemView.findViewById(R.id.txtMotivoCita);
            txtEstado = itemView.findViewById(R.id.txtEstadoCita);
        }
    }
}
