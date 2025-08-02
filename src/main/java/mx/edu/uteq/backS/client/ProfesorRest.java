package mx.edu.uteq.backS.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mx.edu.uteq.backS.model.dto.Profesor;

@FeignClient(name = "servicio-profesores", url = "http://localhost:8085")
public interface ProfesorRest {

    @GetMapping("/api/profesor") 
    List<Profesor> getProfesoresActivos(@RequestParam("activos") boolean activos); 
}
