package com.gabriel.utils;

import com.gabriel.repositories.UserRepository;
import com.gabriel.services.operator.AdminOperatorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Runner para inicializar proyecto con algunos datos de base
 * Solo para pruebas, no tests.
 */
@Component //Para que se ejecute al iniciar el proyect
public class Runner implements CommandLineRunner{

    private final UserRepository userRepository;
    private final AdminOperatorService adminOperatorService;

    public Runner(UserRepository userRepository, AdminOperatorService adminOperatorService) {
        this.userRepository = userRepository;
        this.adminOperatorService = adminOperatorService;
    }

    @Override
    public void run(String... args) throws Exception {
    }


}
