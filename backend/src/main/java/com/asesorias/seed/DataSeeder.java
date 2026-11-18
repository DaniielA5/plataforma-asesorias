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
import org.springframework.beans.factory.annotation.Value;

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
    private static final String PASSWORD_PROFESOR = "Profesor123!";
    private static final String PASSWORD_ALUMNO = "Alumno123!";

    @Value("${seed.alumno.correo:carlos.juarez@asesorias.com}")
    private String correoAlumnoPrueba;

    @Value("${seed.alumno.telefono:+529510000001}")
    private String telefonoAlumnoPrueba;

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
        List<Usuario> profesores = crearProfesores(materias);
        List<Usuario> alumnos = crearAlumnos();
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

        private List<Usuario> crearProfesores(List<Materia> materias) {
        Rol rolProfesor = buscarRol(NombreRol.PROFESOR);

        String[][] datos = {
            {"Aracely Ramirez", "aracely.ramirez@asesorias.com"},
            {"Adelina Martinez", "adelina.mtz@asesorias.com"},
            {"Bibiana Diaz", "bibiana.diaz@asesorias.com"},
            {"Luis Alonso", "luis.alonso@asesorias.com"}
        };

        // Posiciones (0 a 11) de las materias que imparte cada profesor
        int[][] materiasDeCadaProfesor = {
            {0, 1, 3},
            {4, 5, 11},
            {1, 2, 7, 8},
            {6, 9, 10}
        };

        List<Usuario> profesores = new ArrayList<>();
        for (int i = 0; i < datos.length; i++) {
            Usuario profesor = crearUsuario(rolProfesor, datos[i][0], datos[i][1], PASSWORD_PROFESOR, null);
            for (int posicion : materiasDeCadaProfesor[i]) {
                profesor.getMateriasImpartidas().add(materias.get(posicion));
            }
            profesores.add(usuarioRepository.save(profesor));
        }

        log.info("Seeder: {} profesores creados.", profesores.size());
        return profesores;
    }


        private List<Usuario> crearAlumnos() {
        Rol rolAlumno = buscarRol(NombreRol.ALUMNO);

        String[][] datos = {
            {"Isaac Diaz", correoAlumnoPrueba, telefonoAlumnoPrueba},
            {"María Hernández", "maria.hernandez@asesorias.com", "+529510000002"},
            {"Diego Pérez", "diego.perez@asesorias.com", "+529510000003"},
            {"Ana López", "ana.lopez@asesorias.com", "+529510000004"},
            {"Jorge Santos", "jorge.santos@asesorias.com", "+529510000005"},
            {"Valeria Cruz", "valeria.cruz@asesorias.com", "+529510000006"},
            {"Mateo Ramos", "mateo.ramos@asesorias.com", "+529510000007"},
            {"Camila Torres", "camila.torres@asesorias.com", "+529510000008"},
            {"Pablo Vargas", "pablo.vargas@asesorias.com", "+529510000009"},
            {"Fernanda Ortiz", "fernanda.ortiz@asesorias.com", "+529510000010"}
        };

        List<Usuario> alumnos = new ArrayList<>();
        for (String[] fila : datos) {
            alumnos.add(crearUsuario(rolAlumno, fila[0], fila[1], PASSWORD_ALUMNO, fila[2]));
        }

        log.info("Seeder: {} alumnos creados.", alumnos.size());
        return alumnos;
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