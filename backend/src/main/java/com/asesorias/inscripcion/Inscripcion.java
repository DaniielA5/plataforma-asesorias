package com.asesorias.inscripcion;

import com.asesorias.asesorias.Asesoria;
import com.asesorias.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "inscripciones",
    uniqueConstraints = @UniqueConstraint(
        name = "uq_inscripcion_asesoria_alumno",
        columnNames = {"asesoria_id", "alumno_id"}
    )
)
@Getter
@Setter
@NoArgsConstructor
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asesoria_id", nullable = false)
    private Asesoria asesoria;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Usuario alumno;

    @Column(name = "inscrito_en", nullable = false, updatable = false)
    private LocalDateTime inscritoEn;

    @PrePersist
    void alCrear() {
        if (inscritoEn == null) {
            inscritoEn = LocalDateTime.now();
        }
    }
}