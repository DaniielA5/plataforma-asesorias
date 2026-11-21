package com.asesorias.usuario;

import java.time.LocalDateTime;

public record UsuarioAdminResponse(
        Long id,
        String nombre,
        String correo,
        String rol,
        String telefono,
        boolean activo,
        LocalDateTime creadoEn
) {

    public static UsuarioAdminResponse de(Usuario usuario) {
        return new UsuarioAdminResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRol().getNombre().name(),
                usuario.getTelefono(),
                usuario.isActivo(),
                usuario.getCreadoEn()
        );
    }
}