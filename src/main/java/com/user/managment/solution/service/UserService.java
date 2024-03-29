package com.user.managment.solution.service;

import com.user.managment.solution.exception.UserNotFoundException;
import com.user.managment.solution.model.User;
import com.user.managment.solution.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public User saveUser(User user) {
        log.info("Saving user into db: {}", user);
        return userRepository.save(user);
    }

    public User findByEmail(String email) throws UserNotFoundException {
        log.info("Fetching user with email: {} from Database", email);

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with Email address: " + email + " not found"));
    }

    public List<User> findAllUsers() {
        List<User> users = userRepository.findAll();
        log.info("Fetching all users from DB: {}", users);

        return users;
    }

    public void deleteUser(String email) throws UserNotFoundException {
        log.info("Deleting user from db email: {}", email);

        String userId = userRepository.findAll()
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .map(User::getId)
                .findAny()
                .orElseThrow(() -> new UserNotFoundException("User with Email address: " + email + " not found"));

        userRepository.deleteById(userId);
    }

    public User editUser(String email, User newUser) throws UserNotFoundException {
        User foundUser = this.findByEmail(email);

        newUser.setId(foundUser.getId());

        log.info("Updating user with id: {} data: {}", newUser.getId(), newUser);
        return userRepository.save(newUser);
    }
}
