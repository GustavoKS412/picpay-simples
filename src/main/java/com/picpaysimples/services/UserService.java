package com.picpaysimples.services;

import com.picpaysimples.domain.user.User;
import com.picpaysimples.domain.user.UserType;
import com.picpaysimples.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {
        if (sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("Usuários do tipo MERCHANT não estão autorizados a realizar transações");
        }
        if (sender.getBalance().compareTo(amount) < 0)
            throw new Exception("Saldo insuficiente para realizar a transação");
    }
    public User findUserById(Long id) throws Exception {
        return this.repository.findUserById(id).orElseThrow(() -> new Exception("Usuário não encontrado para o ID:" + id));
    }
    public void saveUser(User user){
        this.repository.save(user);
    }
}
