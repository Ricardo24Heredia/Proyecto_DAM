package sv.edu.ues.dam135.appcitas.ui.pacientes.adapter;

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
import sv.edu.ues.dam135.appcitas.data.Paciente;
import sv.edu.ues.dam135.appcitas.ui.pacientes.PacienteFormDialog;

public class PacienteLecturaAdapter extends RecyclerView.Adapter<PacienteLecturaAdapter.PacienteViewHolder> {

    private List<Paciente> lista;

    public PacienteLecturaAdapter(List<Paciente> lista) {
        this.lista = lista;
    }

    public void setLista(List<Paciente> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PacienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paciente_lectura, parent, false);
        return new PacienteViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull PacienteViewHolder holder, int position) {
        Paciente paciente = lista.get(position);

        holder.txtNombre.setText(" Nombre: " + paciente.nombre);
        holder.txtDUI.setText(" DUI: " + paciente.dui);
        holder.txtTelefono.setText(" Tel: " + paciente.telefono);
        holder.txtEdad.setText(" Edad: " + paciente.edad + " años");
        holder.txtGenero.setText(" Género: " + paciente.genero);
        holder.txtDireccion.setText(" Dirección: " + paciente.direccion);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class PacienteViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtDUI, txtTelefono, txtEdad, txtGenero, txtDireccion;

        public PacienteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombrePaciente);
            txtDUI = itemView.findViewById(R.id.txtDUIPaciente);
            txtTelefono = itemView.findViewById(R.id.txtTelefonoPaciente);
            txtEdad = itemView.findViewById(R.id.txtEdadPaciente);
            txtGenero = itemView.findViewById(R.id.txtGeneroPaciente);
            txtDireccion = itemView.findViewById(R.id.txtDireccionPaciente);
        }
    }
}
