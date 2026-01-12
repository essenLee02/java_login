package com.example.auth.service;

import com.example.auth.model.User;
import com.example.auth.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public boolean emailExists(String email) {
        return repo.findByEmail(email).isPresent();
    }

    public long register(String name, String email, String plainPassword) {
        String hash = BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
        return repo.insert(name, email, hash);
    }

    public Optional<User> authenticate(String email, String plainPassword) {
        var userOpt = repo.findByEmail(email);
        if (userOpt.isEmpty()) return Optional.empty();

        var user = userOpt.get();
        boolean ok = BCrypt.checkpw(plainPassword, user.getPasswordHash());
        return ok ? Optional.of(user) : Optional.empty();
    }
}
