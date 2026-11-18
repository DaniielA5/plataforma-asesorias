package com.asesorias.seed;

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

@Slf4j
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
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
    }

    private void crearAdministrador() {
        Rol rolAdmin = buscarRol(NombreRol.ADMIN);
        crearUsuario(rolAdmin, "Administrador", "admin@asesorias.com", "Admin123!", null);
        log.info("Seeder: administrador creado.");
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