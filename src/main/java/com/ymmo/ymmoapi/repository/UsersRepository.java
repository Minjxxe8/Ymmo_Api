package com.ymmo.ymmoapi.repository;

import com.ymmo.ymmoapi.model.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);
    boolean existsUsersByEmail(String email);
    boolean existsUsersById(int id);
    Users findById(int id);

    Users removeUsersById(int id);
}
