package mx.edu.uteq.backS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.edu.uteq.backS.client.ProfesorRest;
import mx.edu.uteq.backS.model.dto.Profesor;

@Service
public class SolicitudesService {

    @Autowired
    private ProfesorRest profesorRest;

    public List<Profesor> obtenerProfesoresActivos() {
        return profesorRest.getProfesoresActivos(true);
    }

}
