package mx.edu.uteq.backS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
