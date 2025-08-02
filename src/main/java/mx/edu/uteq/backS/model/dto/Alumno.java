package mx.edu.uteq.backS.model.dto;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class Alumno {
    @Id
    private int id;
    private String nombre;
    private String matricula;
    private String grupo;
}
