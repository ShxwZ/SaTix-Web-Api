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
//        String password = "1234";
//        User operador = new User(
//                "SaTix-Test",
//                new BCryptPasswordEncoder().encode(password)
//        );
//        userRepository.save(operador);
//
//        User admin = userRepository.getUserAdminByUsername("Nachin").orElse(null);
//        AdminOperator adminOperator = new AdminOperator(operador,admin,password);
//        adminOperatorService.createAdminOperator(adminOperator);

    }


    /*@Bean
    CommandLineRunner iniData(UserRepository userRepository){
        return (args) -> {
            User yo = new User(
                    "Gabriel",
                    "Garcia",
                    "Pellizzón",
                    "Shaw",
                    "617664361",
                    "75727300",
                    "gabriagp02@gmail.com",
                    "1234",
                    User.TypeUser.ADMIN,
                    LocalDateTime.of(2001,10,2,0,0)
                    );
            userRepository.save(yo);
            userRepository.findAll().forEach(System.out::println);
        };
            @Bean
    CommandLineRunner iniData(UserRepository userRepository, OperatorRepository operatorRepository) {
        return (args) -> {
            operatorRepository.deleteAll(operatorRepository.findAll());
            User user = userRepository.getReferenceById(1L);
            for (int i = 0; i < 20; i++) {
                Operator op = new Operator(
                        "prueba",
                        LocalDateTime.now(),
                        user
                );
                operatorRepository.save(op);
            }

        };
    }
    }*/

}
