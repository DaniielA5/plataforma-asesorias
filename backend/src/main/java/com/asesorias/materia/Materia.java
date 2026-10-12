package com.asesorias.materia;

import org.hibernate.annotations.Collate;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;  

@Entity
@Table(name = "materias")
@Getter 
@Setter 
@NoArgsConstructor 

public class Materia {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20 )
    private String codigo ;
    @Column (nullable = false, length = 120)
    private String nombre; 

}
