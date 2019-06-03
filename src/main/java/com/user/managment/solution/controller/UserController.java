package com.user.managment.solution.controller;

import com.user.managment.solution.exception.UserNotFoundException;
import com.user.managment.solution.model.User;
import com.user.managment.solution.model.dto.UserDTO;
import com.user.managment.solution.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "api/")
public class UserController {

    private final UserService userService;

    private final ModelMapper modelMapper;

    @GetMapping(value = "/users")
    public ResponseEntity<List<UserDTO>> getAll () {
        List<UserDTO> out = userService.findAllUsers()
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(out);
    }

    @PostMapping(value = "/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid UserDTO userDTO) {
        User user = modelMapper.map(userDTO, User.class);

        User savedUser = userService.saveUser(user);

        return ResponseEntity.created(URI.create("users/" + savedUser.getId())).body(userDTO);
    }

    @GetMapping(value = "/users/{email}")
    public ResponseEntity<UserDTO> getUserDetails(@PathVariable String email) throws UserNotFoundException {
        User fetchedUser = userService.findByEmail(email);

        return ResponseEntity.ok(modelMapper.map(fetchedUser, UserDTO.class));
    }

    @DeleteMapping(value = "/users/{email}")
    public ResponseEntity deleteUser(@PathVariable String email) throws UserNotFoundException {
        userService.deleteUser(email);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/users/{email}")
    public ResponseEntity<UserDTO> editUser(@PathVariable String email, @RequestBody UserDTO userDTO) throws UserNotFoundException {
        User user = userService.editUser(email, modelMapper.map(userDTO, User.class));
        return ResponseEntity.ok(modelMapper.map(user, UserDTO.class));
    }
}
