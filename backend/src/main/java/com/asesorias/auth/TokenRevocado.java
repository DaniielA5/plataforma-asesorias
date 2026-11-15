package com.asesorias.auth;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens_revocados")
@Getter
@Setter
@NoArgsConstructor
public class TokenRevocado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String jti;

    @Column(name = "expira_en", nullable = false)
    private LocalDateTime expiraEn;

    @Column(name = "revocado_en", nullable = false, updatable = false)
    private LocalDateTime revocadoEn;

    @PrePersist
    void alCrear() {
        if (revocadoEn == null) {
            revocadoEn = LocalDateTime.now();
        }
    }
}