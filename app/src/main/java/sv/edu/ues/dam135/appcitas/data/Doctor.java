package sv.edu.ues.dam135.appcitas.data;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "doctores",
        indices = {@Index(value = {"dui"}, unique = true)})
public class Doctor {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "dui")
    public String dui;

    @ColumnInfo(name = "nombre")
    public String nombre;

    @ColumnInfo(name = "edad")
    public int edad;

    @ColumnInfo(name = "especialidad")
    public String especialidad;

    @ColumnInfo(name = "telefono")
    public String telefono;

    @ColumnInfo(name = "direccion")
    public String direccion;

}
