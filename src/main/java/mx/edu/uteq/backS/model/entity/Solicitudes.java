package mx.edu.uteq.backS.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 *
 * @author aidal
 */
@Entity
@Data
public class Solicitudes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int id; 
    private String estado;
    private int idProfesor;
    private int idAlumno;
}
