package sv.edu.ues.dam135.appcitas.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.edu.ues.dam135.appcitas.data.Paciente;

@Dao
public interface PacienteDao {

    @Query("SELECT * FROM pacientes ORDER BY nombre ASC")
    List<Paciente> obtenerTodos();

    @Insert
    void insertar(Paciente paciente);

    @Update
    void actualizar(Paciente paciente);

    @Delete
    void eliminar(Paciente paciente);

    @Query("SELECT * FROM pacientes WHERE dui = :dui LIMIT 1")
    Paciente obtenerPorDui(String dui);

}
