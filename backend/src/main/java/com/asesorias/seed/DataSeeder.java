package com.asesorias.seed;

import com.asesorias.materia.Materia;
import com.asesorias.materia.MateriaRepository;
import com.asesorias.usuario.NombreRol;
import com.asesorias.usuario.Rol;
import com.asesorias.usuario.RolRepository;
import com.asesorias.usuario.Usuario;
import com.asesorias.usuario.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final MateriaRepository materiaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            log.info("Seeder: la base ya tiene usuarios, no se inserta nada.");
            return;
        }

        log.info("Seeder: base vacía, insertando datos de ejemplo...");

        crearAdministrador();
        List<Materia> materias = crearMaterias();
    }

    private void crearAdministrador() {
        Rol rolAdmin = buscarRol(NombreRol.ADMIN);
        crearUsuario(rolAdmin, "Administrador", "admin@asesorias.com", "Admin123!", null);
        log.info("Seeder: administrador creado.");
    }

    private List<Materia> crearMaterias() {
        String[][] datos = {
            {"MAT-101", "Cálculo integral"},
            {"MAT-102", "Cálculo diferencial"},
            {"MAT-103", "Geometría analítica"},
            {"MAT-104", "Álgebra lineal"},
            {"QUI-101", "Química orgánica"},
            {"QUI-102", "Química inorgánica"},
            {"BIO-101", "Biología celular"},
            {"FIS-101", "Física mecánica"},
            {"FIS-102", "Electricidad y magnetismo"},
            {"PRO-101", "Programación básica"},
            {"PRO-102", "Estructuras de datos"},
            {"EST-101", "Probabilidad y estadística"}
        };

        List<Materia> materias = new ArrayList<>();
        for (String[] fila : datos) {
            Materia materia = new Materia();
            materia.setCodigo(fila[0]);
            materia.setNombre(fila[1]);
            materias.add(materia);
        }

        List<Materia> guardadas = materiaRepository.saveAll(materias);
        log.info("Seeder: {} materias creadas.", guardadas.size());
        return guardadas;
    }

    private Rol buscarRol(NombreRol nombre) {
        return rolRepository.findByNombre(nombre)
                .orElseThrow(() -> new IllegalStateException(
                        "Falta el rol " + nombre + " en la base. Revisa las migraciones de Flyway."));
    }

    private Usuario crearUsuario(Rol rol, String nombre, String correo, String password, String telefono) {
        Usuario usuario = new Usuario();
        usuario.setRol(rol);
        usuario.setNombre(nombre);
        usuario.setCorreo(correo);
        usuario.setPasswordHash(passwordEncoder.encode(password));
        usuario.setTelefono(telefono);
        return usuarioRepository.save(usuario);
    }
}