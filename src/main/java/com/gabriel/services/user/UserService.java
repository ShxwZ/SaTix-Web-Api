package com.gabriel.services.user;

import com.gabriel.dto.ErrorRegisterResponse;
import com.gabriel.dto.RegisterDTO;
import com.gabriel.models.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User getUserAdminByUsername(String username);
    User getUserByUsername(String username);
    User getUserById(Long id);
    void saveUser(User user);
    boolean removeUser(User user);
    ErrorRegisterResponse checkInfoRegister(RegisterDTO registerDTO);

    boolean register(RegisterDTO registerDTO);
}
