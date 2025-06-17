package sv.edu.ues.dam135.appcitas.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import sv.edu.ues.dam135.appcitas.data.Doctor;

@Dao
public interface DoctorDao {
    @Insert
    void insertar(Doctor doctor);

    @Update
    void actualizar(Doctor doctor);

    @Delete
    void eliminar(Doctor doctor);

    @Query("SELECT * FROM doctores")
    List<Doctor> obtenerTodos();

}
