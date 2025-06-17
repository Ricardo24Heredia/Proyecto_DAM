package sv.edu.ues.dam135.appcitas.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "citas")
public class Cita {

    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "dui_paciente")
    public String duiPaciente;

    @ColumnInfo(name = "nombre_paciente")
    public String nombrePaciente;

    @ColumnInfo(name = "id_doctor")
    public int idDoctor;

    @ColumnInfo(name = "nombre_doctor")
    public String nombreDoctor;

    @ColumnInfo(name = "especialidad")
    public String especialidad;

    @ColumnInfo(name = "fecha")
    public String fecha;

    @ColumnInfo(name = "hora")
    public String hora;

    @ColumnInfo(name = "motivo")
    public String motivo;

    @ColumnInfo(name = "estado")
    public String estado;
}
