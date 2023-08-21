package ru.hogwarts.school.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.Service.InfoService;
import ru.hogwarts.school.Service.InfoServiceImpl;

@RequestMapping("/info")
@RestController
public class InfoController {

    private final InfoService infoService;
    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    @GetMapping("/getPort")
    public ResponseEntity<String> getPortInfo() {
        return ResponseEntity.ok(infoService.getPort());
    }

}
