package com.gabriel.services.operator;

import com.gabriel.models.OperatorTableView;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminOperatorRepositoryImpl {
    boolean createAdminOperator(String usernameAdmin);
    void removeOperator(Long id, String usernameAdmin);
    List<OperatorTableView> getAllOperatorTableViewByAdmin(String username);

}
