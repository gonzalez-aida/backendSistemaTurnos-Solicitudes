package mx.edu.uteq.backS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mx.edu.uteq.backS.model.dto.Alumno;
import mx.edu.uteq.backS.model.dto.Profesor;
import mx.edu.uteq.backS.service.SolicitudesService;

@RestController
@RequestMapping("/api/solicitud")
public class SolicitudesController {

    @Autowired
    private SolicitudesService serv;

    @GetMapping("/profesores/activos")
    public ResponseEntity<List<Profesor>> getProfesoresActivos() {
        try {
            List<Profesor> profesores = serv.obtenerProfesoresActivos();
            return ResponseEntity.ok(profesores);
        } catch (Exception e) {
            System.err.println("Error al obtener los profesores activos: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{idProfesor}/pendientes")
    public ResponseEntity<List<Alumno>> getAlumnosConSolicitudPendientePorProfesor(
        @PathVariable int idProfesor) {
        try {
            List<Alumno> alumnos = serv.obtenerAlumnosConSolicitudPendientePorProfesor(idProfesor);
            return ResponseEntity.ok(alumnos);
        } catch (Exception e) {
            System.err.println("Error al obtener alumnos con solicitud pendiente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{idProfesor}/rev-fin")
    public ResponseEntity<List<Alumno>> getAlumnosConSolicitudRevFinPorProfesor(
        @PathVariable int idProfesor) {
        try {
            List<Alumno> alumnos = serv.obtenerAlumnosConSolicitudRevFinPorProfesor(idProfesor);
            return ResponseEntity.ok(alumnos);
        } catch (Exception e) {
            System.err.println("Error al obtener alumnos con solicitud pendiente: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<String> actualizarEstado(
            @PathVariable Integer id,
            @RequestParam String estado) {
        
        serv.actualizarEstado(id, estado);
        return ResponseEntity.ok("Estado actualizado");
    }

}
