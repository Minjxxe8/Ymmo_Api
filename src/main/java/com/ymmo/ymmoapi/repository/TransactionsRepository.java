package com.ymmo.ymmoapi.repository;

import com.ymmo.ymmoapi.model.Properties;
import com.ymmo.ymmoapi.model.Transactions;
import com.ymmo.ymmoapi.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionsRepository extends JpaRepository<Transactions, Integer> {
    Optional<List<Transactions>> findByUsers(Users users);
    Optional<Transactions> findById(int id);
    boolean existsByUsersAndProperties(Users users, Properties properties);
}
