package sv.edu.ues.dam135.appcitas.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import sv.edu.ues.dam135.appcitas.data.Usuario;

@Dao
public interface UsuarioDao {

    @Insert
    void insertar(Usuario usuario);

    @Query("SELECT * FROM usuarios WHERE correo = :correo AND contrasena = :contrasena")
    Usuario login(String correo, String contrasena);

    @Query("SELECT * FROM usuarios WHERE correo = :correo")
    Usuario obtenerPorCorreo(String correo);
}
