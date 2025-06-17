package sv.edu.ues.dam135.appcitas.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import sv.edu.ues.dam135.appcitas.dao.CitaDao;
import sv.edu.ues.dam135.appcitas.dao.DoctorDao;
import sv.edu.ues.dam135.appcitas.dao.PacienteDao;
import sv.edu.ues.dam135.appcitas.dao.UsuarioDao;

@Database(entities = {Usuario.class, Paciente.class, Doctor.class, Cita.class}, version = 3)

public abstract class AppDatabase extends RoomDatabase {
    public abstract UsuarioDao usuarioDao();
    public abstract PacienteDao pacienteDao();
    public abstract DoctorDao doctorDao();
    public abstract CitaDao citaDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "citas_db")
                    .build();
        }
        return INSTANCE;
    }
}
