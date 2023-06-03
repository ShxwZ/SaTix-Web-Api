package com.gabriel.repositories;

import com.gabriel.models.AdminOperator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminOperatorRepository extends JpaRepository<AdminOperator, Long> {

    @Query("SELECT ao FROM AdminOperator ao WHERE ao.admin.username = ?1")
    List<AdminOperator> getByOperatorByAdminUsername(String username);
    @Query("SELECT ao FROM AdminOperator ao WHERE ao.operator.id = ?1 AND ao.admin.id = ?2")
    AdminOperator getByOperatorIdAndAdminId(Long operatorId, Long adminId);
}
