package mx.edu.uteq.backS.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import mx.edu.uteq.backS.service.SolicitudesService;

@RestController
@RequestMapping("/api/solicitud")
public class SolicitudesController {

    @Autowired
    private SolicitudesService serv;

}
