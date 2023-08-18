package ru.hogwarts.school.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class InfoServiceImpl implements InfoService{

    @Value("${server.port}")
    private String port;

    public String getPort(){
        return port;
    }
}
