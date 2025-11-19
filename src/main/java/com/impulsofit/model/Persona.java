package com.impulsofit.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "persona")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Long idPersona;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "nombres", nullable = false)
    private String nombres;

    @Column(name = "apellido_p", nullable = false)
    private String apellidoP;

    @Column(name = "apellido_m", nullable = false)
    private String apellidoM;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "genero", nullable = false)
    private String genero;

    public String getNombres() {
        StringBuilder sb = new StringBuilder();
        if (nombres != null) sb.append(nombres);
        if (apellidoP != null) sb.append(" ").append(apellidoP);
        if (apellidoM != null) sb.append(" ").append(apellidoM);
        return sb.toString().trim();
    }
}

