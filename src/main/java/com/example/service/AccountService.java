package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.entity.Account;
import com.example.repository.AccountRepository;

import java.util.Optional;

import javax.naming.AuthenticationException;

@Service
@Transactional
public class AccountService {
    AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public Account registerAccount(Account account) {
        // Validate username and password
        if (account.getUsername() == null || account.getUsername().isBlank() ||
            account.getPassword() == null || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        // Check if username already exists
        Optional<Account> existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount.isPresent()) {
            throw new IllegalStateException("Username already exists");
        }

        // Save the new account
        return accountRepository.save(account);
    }

    public Account loginAccount(Account account) throws AuthenticationException {
        // Check if username already exists
        Optional<Account> existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount.isPresent()) {
            if (account.getPassword().equals(existingAccount.get().getPassword())) {
                return existingAccount.get();
            }

            throw new AuthenticationException("Invalid login credentials");

        } else {
            throw new AuthenticationException("Invalid login credentials");
        }
    }
}
