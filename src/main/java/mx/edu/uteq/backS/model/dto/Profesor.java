package mx.edu.uteq.backS.model.dto;

import jakarta.persistence.Id;
import lombok.Data;

@Data
public class Profesor {
    @Id
    private int id;
    private String nombre;
    private String correo;
    private String cubiculo;
    private boolean activo;
}
