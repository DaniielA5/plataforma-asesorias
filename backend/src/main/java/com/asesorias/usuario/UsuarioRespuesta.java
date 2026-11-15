package com.asesorias.usuario;

public record UsuarioRespuesta(
        Long id,
        String nombre,
        String correo,
        String rol,
        String telefono,
        String avatarUrl
) {

    public static UsuarioRespuesta de(Usuario usuario) {
        return new UsuarioRespuesta(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRol().getNombre().name(),
                usuario.getTelefono(),
                usuario.getAvatarUrl()
        );
    }
}