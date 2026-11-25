package com.asesorias.auth;

public record CuentaGoogle(
    String sub, 
    String correo,
    String nombre,
    boolean correoConfiable
) {
    
}
