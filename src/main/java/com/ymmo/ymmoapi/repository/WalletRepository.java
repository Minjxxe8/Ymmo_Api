package com.ymmo.ymmoapi.repository;

import com.ymmo.ymmoapi.model.Users;
import com.ymmo.ymmoapi.model.Wallets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallets, Integer> {
    Wallets findByUser(Users user);

    boolean existsByUser(Users user);
}
