package sv.edu.ues.dam135.appcitas.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "usuarios")
public class Usuario {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "nombre")
    public String nombre;

    @ColumnInfo(name = "correo")
    public String correo;

    @ColumnInfo(name = "contrasena")
    public String contrasena;
}
