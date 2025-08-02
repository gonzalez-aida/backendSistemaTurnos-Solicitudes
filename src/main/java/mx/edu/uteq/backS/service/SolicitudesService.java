package mx.edu.uteq.backS.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.edu.uteq.backS.client.AlumnoRest;
import mx.edu.uteq.backS.client.ProfesorRest;
import mx.edu.uteq.backS.model.dto.Alumno;
import mx.edu.uteq.backS.model.dto.Profesor;
import mx.edu.uteq.backS.model.entity.Solicitudes;
import mx.edu.uteq.backS.model.repository.SolicitudesRepo;

@Service
public class SolicitudesService {

    @Autowired
    private ProfesorRest profesorRest;

    @Autowired
    private AlumnoRest alumnoRest;

    @Autowired
    private SolicitudesRepo repo;

    public List<Profesor> obtenerProfesoresActivos() {
        return profesorRest.getProfesoresActivos(true);
    }

    public List<Alumno> obtenerAlumnosConSolicitudPendientePorProfesor(int idProfesor) {
    List<Solicitudes> solicitudesPendientes = repo.findByEstadoAndIdProfesor("Pendiente", idProfesor);
    
    List<Integer> idsAlumnos = solicitudesPendientes.stream()
            .map(Solicitudes::getIdAlumno)
            .distinct()
            .collect(Collectors.toList());
    
    if(idsAlumnos.isEmpty()) {
        return Collections.emptyList();
    }
    
    return alumnoRest.getAlumnosByIds(idsAlumnos);
}

    public List<Alumno> obtenerAlumnosConSolicitudRevFinPorProfesor(int idProfesor) {
    List<String> estados = Arrays.asList("Revision", "Finalizada");
    
    List<Solicitudes> solicitudesRevFin = repo.findByIdProfesorAndEstadoIn(idProfesor, estados);
    
    List<Integer> idsAlumnos = solicitudesRevFin.stream()
            .map(Solicitudes::getIdAlumno)
            .distinct()
            .collect(Collectors.toList());
    
    if(idsAlumnos.isEmpty()) {
        return Collections.emptyList();
    }
    
    return alumnoRest.getAlumnosByIds(idsAlumnos);
}

    public void actualizarEstado(int idSolicitud, String nuevoEstado) {
        Solicitudes solicitud = repo.findById(idSolicitud)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        
        solicitud.setEstado(nuevoEstado);
        repo.save(solicitud);  // Guardado explícito para mayor seguridad
    }

}
