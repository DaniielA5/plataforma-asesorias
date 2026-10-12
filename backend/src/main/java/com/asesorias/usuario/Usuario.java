package com.asesorias.usuario;
import com.asesorias.materia.Materia;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;



@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor 
public class Usuario {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique= false, length = 120)
    private String correo;

    @Column(name = "password_hash", nullable = false, length = 255 ) 
    private  String passwString;

    @Column(nullable = false, length = 20 )
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20 )
    private Rol rol;

    @Column(name = "creado_en", nullable = false, updatable = false)
    private LocalDateTime creadoEn = LocalDateTime.now();

    @ManyToMany
    @JoinTable(
        name = "profesor_materia",
        joinColumns = @JoinColumn(name = "profesor_id"),
        inverseJoinColumns = @JoinColumn(name = "materia_id")
    )
    private Set<Materia> materiasImpartidas = new HashSet<>();
}    


