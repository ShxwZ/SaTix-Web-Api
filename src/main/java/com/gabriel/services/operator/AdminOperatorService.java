package com.gabriel.services.operator;

import com.gabriel.models.AdminOperator;
import com.gabriel.models.OperatorTableView;
import com.gabriel.models.User;
import com.gabriel.repositories.AdminOperatorRepository;
import com.gabriel.services.user.UserService;
import com.gabriel.utils.PasswordGenerator;
import com.gabriel.utils.UsernameGenerator;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminOperatorService implements AdminOperatorRepositoryImpl{

    private final AdminOperatorRepository adminOperatorRepository;
    private final UserService userService;

    public AdminOperatorService(AdminOperatorRepository adminOperatorRepository, UserService userService) {
        this.adminOperatorRepository = adminOperatorRepository;
        this.userService = userService;
    }
    /**
     * Crea un nuevo operador administrativo asociado al administrador especificado.
     * Genera automáticamente un nombre de usuario y una contraseña para el operador.
     *
     * @param usernameAdmin nombre de usuario del administrador que crea el operador.
     * @return true si se crea correctamente, false en caso contrario.
     */
    @Override
    public boolean createAdminOperator(String usernameAdmin) {
        String password = PasswordGenerator.generatePassword();
        try {
            User operator = new User(
                    UsernameGenerator.generateUniqueUsername(userService),
                    new BCryptPasswordEncoder().encode(password)
            );
            userService.saveUser(operator);

            User admin = userService.getUserAdminByUsername(usernameAdmin);
            AdminOperator adminOperator = new AdminOperator(admin,operator,password);

            adminOperatorRepository.save(adminOperator);
            return true;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    /**
     * Elimina un operador con el ID especificado si existe y está asociado al administrador especificado.
     *
     * @param id            ID del operador que se desea eliminar.
     * @param usernameAdmin nombre de usuario del administrador que realiza la eliminación.
     */
    @Override
    public void removeOperator(Long id, String usernameAdmin) {
        User admin = userService.getUserAdminByUsername(usernameAdmin);
        if (admin == null) return;
        AdminOperator adminOperator = adminOperatorRepository.getByOperatorIdAndAdminId(id,admin.getId());
        adminOperatorRepository.delete(adminOperator);
        userService.removeUser(adminOperator.getOperator());
    }
    /**
     * Obtiene una lista de todos los operadores asociados al administrador en formato de tabla de operadores.
     *
     * @param username nombre de usuario del administrador.
     * @return una lista de OperatorTableView.
     */

    @Override
    public List<OperatorTableView> getAllOperatorTableViewByAdmin(String username) {
        List<AdminOperator> operators = adminOperatorRepository.getByOperatorByAdminUsername(username);
        List<OperatorTableView> operatorTableViews = new ArrayList<>();
        for (AdminOperator operator : operators) {
            operatorTableViews.add(
                    new OperatorTableView(
                            operator.getOperator().getId(),
                            operator.getOperator().getUsername(),
                            operator.getPasswordNoEncode(),
                            operator.getOperator().getDate_creation()
                    )
            );
        }
        return operatorTableViews;
    }
}
