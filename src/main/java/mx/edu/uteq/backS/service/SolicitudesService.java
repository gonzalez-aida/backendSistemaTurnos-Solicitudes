package mx.edu.uteq.backS.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import feign.FeignException;
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
        repo.save(solicitud);  
    }


    
    public Integer obtenerIdGrupoPorMatricula(String matricula) {
        try {
            ResponseEntity<Alumno> response = alumnoRest.getByMatricula(matricula);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getIdGrupo();
            }
            throw new RuntimeException("Alumno no encontrado o sin grupo asignado");
        } catch (FeignException e) {
            System.err.println("Error en Feign: " + e.status() + " - " + e.contentUTF8());
            throw new RuntimeException("Error al llamar al servicio de alumnos");
        }
    }


    public List<Profesor> obtenerProfesoresPorMatriculaAlumno(String matricula) {
        Integer idGrupo = obtenerIdGrupoPorMatricula(matricula);
        if (idGrupo != null) {
            try {
                ResponseEntity<List<Profesor>> response = profesorRest.getProfesoresByGrupo(idGrupo);
                
                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    return response.getBody();
                } else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw new RuntimeException("No se encontraron profesores para el grupo con ID: " + idGrupo);
                }
            } catch (FeignException e) {
                System.err.println("Error en Feign: " + e.status() + " - " + e.contentUTF8());
                throw new RuntimeException("Error al llamar al servicio de profesores");
            }
        }
        throw new RuntimeException("No se pudo obtener el grupo del alumno, o no se encontraron profesores");
    }

    public List<Map<String, Object>> obtenerSolicitudesConDetalles(String matricula) {
        Integer idAlumno = obtenerIdAlumnoPorMatricula(matricula);
        if (idAlumno == null) {
            throw new RuntimeException("Alumno con matrícula " + matricula + " no encontrado.");
        }

        List<Solicitudes> solicitudes = repo.findByIdAlumno(idAlumno);

        List<Map<String, Object>> solicitudesConDetalle = solicitudes.stream()
            .map(solicitud -> {
                Map<String, Object> detalleSolicitud = new HashMap<>();
                detalleSolicitud.put("estatus", solicitud.getEstado());
                
                try {
                    ResponseEntity<Profesor> profesorResponse = profesorRest.getById(solicitud.getIdProfesor());
                    Profesor profesor = profesorResponse.getBody();
    
                    if (profesor != null) {
                        detalleSolicitud.put("nombreMaestro", profesor.getNombre());
                        detalleSolicitud.put("cubiculoMaestro", profesor.getCubiculo());
                    } else {
                        detalleSolicitud.put("nombreMaestro", "No disponible");
                        detalleSolicitud.put("cubiculoMaestro", "No disponible");
                    }
                } catch (FeignException e) {
                    System.err.println("Error al obtener datos del profesor con ID " + solicitud.getIdProfesor() + ": " + e.status());
                    detalleSolicitud.put("nombreMaestro", "Error de servicio");
                    detalleSolicitud.put("cubiculoMaestro", "Error de servicio");
                }
                return detalleSolicitud;
            })
            .collect(Collectors.toList());

        return solicitudesConDetalle;
    }

    private Integer obtenerIdAlumnoPorMatricula(String matricula) {
        try {
            ResponseEntity<Alumno> response = alumnoRest.getByMatricula(matricula);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getId();
            }
            return null;
        } catch (FeignException e) {
            System.err.println("Error al llamar al servicio de alumnos: " + e.status());
            return null;
        }
    }


    public Solicitudes crearNuevaSolicitud(Map<String, Object> solicitudData) {
        Integer idAlumno = (Integer) solicitudData.get("idAlumno");
        Integer idProfesor = (Integer) solicitudData.get("idProfesor");

        if (idAlumno == null || idProfesor == null) {
            throw new IllegalArgumentException("El ID del alumno y del profesor son campos requeridos.");
        }
        Solicitudes nuevaSolicitud = new Solicitudes();
        nuevaSolicitud.setIdAlumno(idAlumno);
        nuevaSolicitud.setIdProfesor(idProfesor);
        nuevaSolicitud.setEstado("Pendiente"); 

        return repo.save(nuevaSolicitud);
    }
}
