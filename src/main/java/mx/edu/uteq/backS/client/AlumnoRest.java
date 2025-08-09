package mx.edu.uteq.backS.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import mx.edu.uteq.backS.model.dto.Alumno;

@FeignClient(name = "alumnos", url = "http://localhost:8080")
public interface AlumnoRest {

    @PostMapping("/api/alumnos/por-ids")
    List<Alumno> getAlumnosByIds(@RequestBody List<Integer> ids);

    @GetMapping("/api/alumnos/matricula/{matricula}")
    ResponseEntity<Alumno> getByMatricula(@PathVariable String matricula);

    
}
