package mx.edu.uteq.backS.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import mx.edu.uteq.backS.model.dto.Alumno;
import mx.edu.uteq.backS.model.dto.Profesor;
import mx.edu.uteq.backS.model.entity.Solicitudes;
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


    @GetMapping("/id/{matricula}/grupo")
    public ResponseEntity<Integer> obtenerGrupoAlumno(@PathVariable String matricula) {
        try {
            Integer idGrupo = serv.obtenerIdGrupoPorMatricula(matricula);
            return ResponseEntity.ok(idGrupo);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping("/profesores/{matricula}")
    public ResponseEntity<List<Profesor>> obtenerProfesoresDeAlumno(@PathVariable String matricula) {
        try {
            List<Profesor> profesores = serv.obtenerProfesoresPorMatriculaAlumno(matricula);
            return ResponseEntity.ok(profesores);
        } catch (RuntimeException e) {
            System.err.println("Error al obtener profesores del alumno: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/alumno/{matricula}/detalle")
    public ResponseEntity<List<Map<String, Object>>> getSolicitudesDetalleByMatricula(@PathVariable String matricula) {
        try {
            List<Map<String, Object>> solicitudes = serv.obtenerSolicitudesConDetalles(matricula);
            if (!solicitudes.isEmpty()) {
                return ResponseEntity.ok(solicitudes);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<Solicitudes> crearSolicitud(@RequestBody Map<String, Object> solicitudData) {
        try {
            Solicitudes nuevaSolicitud = serv.crearNuevaSolicitud(solicitudData);
            return new ResponseEntity<>(nuevaSolicitud, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
