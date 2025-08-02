package mx.edu.uteq.backS.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.edu.uteq.backS.model.entity.Solicitudes;
import mx.edu.uteq.backS.model.repository.SolicitudesRepo;

@Service
public class SolicitudesService {

    @Autowired
    private SolicitudesRepo repo;

}
