package com.gabriel.ServicesHelper;

import com.gabriel.dto.ErrorRegisterResponse;
import com.gabriel.dto.RegisterDTO;
import com.gabriel.models.User;
import com.gabriel.services.user.UserService;

public class FakeUserService implements UserService {

    @Override
    public User getUserAdminByUsername(String username) {
        return null;
    }

    @Override
    public User getUserByUsername(String username) {
        // Implementación ficticia que siempre retorna null
        return null;
    }

    @Override
    public User getUserById(Long id) {
        return null;
    }

    @Override
    public void saveUser(User operator) {
    }

    @Override
    public boolean removeUser(User user) {
        return false;
    }

    @Override
    public ErrorRegisterResponse checkInfoRegister(RegisterDTO registerDTO) {
        return null;
    }

    @Override
    public boolean register(RegisterDTO registerDTO) {
        return false;
    }

}