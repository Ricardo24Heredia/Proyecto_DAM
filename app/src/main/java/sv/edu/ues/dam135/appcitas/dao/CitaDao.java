package sv.edu.ues.dam135.appcitas.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.edu.ues.dam135.appcitas.data.Cita;
@Dao
public interface CitaDao {
    @Insert
    void insertar(Cita cita);

    @Update
    void actualizar(Cita cita);

    @Delete
    void eliminar(Cita cita);

    @Query("SELECT * FROM citas")
    List<Cita> obtenerTodas();
    @Query("SELECT * FROM citas WHERE dui_paciente = :dui")
    List<Cita> obtenerCitasPorDui(String dui);

    @Query("SELECT COUNT(*) FROM citas WHERE dui_paciente = :dui")
    int contarCitasPorDuiPaciente(String dui);

    @Query("SELECT COUNT(*) FROM citas WHERE id_doctor = :idDoctor")
    int contarCitasPorDoctor(int idDoctor);

}
