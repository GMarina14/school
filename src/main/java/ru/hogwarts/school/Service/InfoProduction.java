package ru.hogwarts.school.Service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("!test")
public class InfoProduction implements InfoService{
    @Override
    public String getPort() {
        return "Restricted Area";
    }
}
