package com.ymmo.ymmoapi.service;

import com.ymmo.ymmoapi.dto.TransactionCreationDto;
import com.ymmo.ymmoapi.exception.ResourceNotFoundException;
import com.ymmo.ymmoapi.exception.ResponseException;
import com.ymmo.ymmoapi.model.*;
import com.ymmo.ymmoapi.repository.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Component
public class TransactionService {
    private final TransactionsRepository transactionsRepository;
    private final UserService userService;
    private final PropertyService propertyService;
    private final WalletService walletService;

    @Autowired
    public TransactionService(TransactionsRepository transactionsRepository, UserService userService, PropertyService propertyService, WalletService walletService) {
        this.transactionsRepository = transactionsRepository;
        this.userService = userService;
        this.propertyService = propertyService;
        this.walletService = walletService;
    }

    public List<Transactions> getAllTransactions() throws ResourceNotFoundException {
        List<Transactions> transactionsList = transactionsRepository.findAll();
        if (transactionsList.isEmpty()) {
            throw new ResourceNotFoundException("No transactions have been found");
        }
        return transactionsList;
    }

    public Transactions getTransactionById(int id) throws ResourceNotFoundException {
        return transactionsRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction with the id " + id + " has not been found"))
                .getBody();
    }

    public List<Transactions> getTransactionsByUserId(int userId) throws ResourceNotFoundException {
        try {
            Users user = userService.getUserById((long) userId);
            return transactionsRepository.findByUsers(user)
                    .map(ResponseEntity::ok)
                    .orElseThrow(() -> new ResourceNotFoundException("Transactions linked to the user " + userId + " has not been found"))
                    .getBody();
        } catch (ResponseException e) {
            throw new ResponseException(e.getMessage(), e.getHttpCode());
        }
    }

    public Transactions createTransaction(String email, TransactionCreationDto transactionCreationDto) {
        try {
            Users user = userService.getUserByEmail(email);
            System.out.println(user);
            Wallets userWallet = walletService.getUserWalletByEmail(email);
            System.out.println(userWallet);
            Properties property = propertyService.getPropertyById(transactionCreationDto.getPropertyId());
            System.out.println(property);
            if (!userWallet.enoughBalance(property.getPrice())) {
                throw new ResponseException("Your wallet doesn't have enough balance to buy this property", 400);
            }
            walletService.removeBalance(userWallet, property.getPrice());
            propertyService.changePropertyStatus(property.getId(), false);
            return transactionsRepository.save(new TransactionsBuilder()
                    .setDescription(transactionCreationDto.getDescription())
                    .setUsers(user)
                    .setAmount(property.getPrice())
                    .setProperties(property)
                    .build());
        } catch (ResponseException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseException(e.getMessage(), 500);
        }
    }
}
