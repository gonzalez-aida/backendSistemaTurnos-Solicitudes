package mx.edu.uteq.backS.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.edu.uteq.backS.model.entity.Solicitudes;

/**
 *
 * @author aidal
 */
public interface SolicitudesRepo extends JpaRepository<Solicitudes,Integer>{

    List<Solicitudes> findByEstado(String estado);

    List<Solicitudes> findByEstadoAndIdProfesor(String estado, int idProfesor);

    List<Solicitudes> findByIdProfesorAndEstadoIn(int idProfesor, List<String> estados);

}
