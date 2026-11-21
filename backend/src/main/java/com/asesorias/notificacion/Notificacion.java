package com.asesorias.notificacion;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Getter
@Setter
@NoArgsConstructor
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(nullable = false, length = 10)
    private String canal;

    @Column(nullable = false, length = 120)
    private String destino;

    @Column(length = 200)
    private String asunto;

    @Column(nullable = false, length = 10)
    private String estado;

    @Column(length = 500)
    private String error;

    @Column(name = "enviada_en", nullable = false, updatable = false)
    private LocalDateTime enviadaEn;

    @PrePersist
    void alCrear() {
        if (enviadaEn == null) {
            enviadaEn = LocalDateTime.now();
        }
    }
}