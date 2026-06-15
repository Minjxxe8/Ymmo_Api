package com.ymmo.ymmoapi.service;

import com.ymmo.ymmoapi.dto.WalletsDto;
import com.ymmo.ymmoapi.exception.ResourceNotFoundException;
import com.ymmo.ymmoapi.model.Users;
import com.ymmo.ymmoapi.model.Wallets;
import com.ymmo.ymmoapi.repository.UsersRepository;
import com.ymmo.ymmoapi.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final UsersRepository usersRepository;

    public WalletService(WalletRepository walletRepository, UsersRepository usersRepository) {
        this.walletRepository = walletRepository;
        this.usersRepository = usersRepository;
    }

    public void createWallet(Users user) {
        Wallets newWallet = walletRepository.save(new Wallets(
                user,
                0
        ));
        new WalletsDto.WalletResponse(newWallet.getId(), newWallet.getBalance());
    }

    public WalletsDto.WalletResponse getUserWallet(String email) {
        Wallets userWaller = getUserWalletByEmail(email);
        return new WalletsDto.WalletResponse(userWaller.getId(), userWaller.getBalance());
    }

    public WalletsDto.WalletResponse addBalance(String email, int balance) {
        Wallets userWaller = getUserWalletByEmail(email);
        userWaller.addBalance(balance);
        Wallets newValue = walletRepository.save(userWaller);
        return new WalletsDto.WalletResponse(newValue.getId(), newValue.getBalance());
    }

    public WalletsDto.WalletResponse removeBalance(String email, int balance) {
        Wallets userWaller = getUserWalletByEmail(email);
        userWaller.removeBalance(balance);
        Wallets newValue = walletRepository.save(userWaller);
        return new WalletsDto.WalletResponse(newValue.getId(), newValue.getBalance());
    }

    public void removeBalance(Wallets userWaller, double balance) {
        userWaller.removeBalance(balance);
        Wallets newValue = walletRepository.save(userWaller);
        new WalletsDto.WalletResponse(newValue.getId(), newValue.getBalance());
    }

    public Wallets getUserWalletByEmail(String email) {
        return walletRepository.findByUser(
                usersRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with the email : " + email))
        );
    }
}
