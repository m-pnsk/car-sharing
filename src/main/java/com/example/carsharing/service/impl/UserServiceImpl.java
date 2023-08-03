package com.example.carsharing.service.impl;

import com.example.carsharing.model.User;
import com.example.carsharing.repository.UserRepository;
import com.example.carsharing.service.UserService;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public User add(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User getById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("Not found user by id: " + userId)
        );
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    @Override
    public boolean isUserPresentByEmail(String email) {
        return userRepository.getUserByEmail(email).isPresent();
    }

    @Override
    @Transactional
    public User updateUserRole(Long id, User.Role role) {
        User userFromDb = userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Not found user with id: " + id)
        );
        userFromDb.setRole(role);
        return userFromDb;
    }

    @Override
    @Transactional
    public User updateProfileInfo(User user) {
        User userFromDb = userRepository.findById(user.getId()).orElseThrow(
                () -> new RuntimeException("Not found profile info for user: " + user));
        userFromDb.setId(user.getId())
                .setEmail(user.getEmail())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setPassword(encoder.encode(user.getPassword()));
        return userFromDb;
    }
}
