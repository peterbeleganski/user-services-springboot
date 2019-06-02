package com.user.managment.solution.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.user.managment.solution.exception.UserNotFoundException;
import com.user.managment.solution.model.User;
import com.user.managment.solution.model.dto.UserDTO;
import com.user.managment.solution.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest {

    private UserController userController;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @SpyBean
    private ModelMapper modelMapper;

    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void getAll() throws Exception {
        when(userService.findAllUsers()).thenReturn(Collections.singletonList(getUser()));

        mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void createUser() throws Exception {
        User user = getUser();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        when(userService.saveUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(userDTO)));
    }

    @Test
    public void testCreateUserWithExistingEmail() throws Exception {
        User user = getUser();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        when(userService.saveUser(any(User.class))).thenThrow(new DuplicateKeyException("User with this email exists"));

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getUserDetails() throws Exception {
        User user = getUser();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        when(userService.findByEmail(anyString())).thenReturn(user);

        mockMvc.perform(get("/users/" + user.getEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDTO)));
    }

    @Test
    public void testGetUserDetailsForNonExistingUser() throws Exception {
        User user = getUser();
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        when(userService.findByEmail(anyString())).thenThrow(new UserNotFoundException("User with this email does not exist"));

        mockMvc.perform(get("/users/" + user.getEmail())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteUser() throws Exception {
        mockMvc.perform(delete("/users/" + getUser().getEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteUserWithNotExistingEmail() throws Exception {
        doThrow(new UserNotFoundException("User with this email does not exist"))
        .when(userService).deleteUser(anyString());

        mockMvc.perform(delete("/users/" + getUser().getEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void editUser() throws Exception {
        User user = getUser();

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setFirstName("John");
        userDTO.setLastName("Smith");

        User expectedUser = modelMapper.map(userDTO, User.class);
        when(userService.editUser(anyString(), any(User.class))).thenReturn(expectedUser);

        mockMvc.perform(put("/users/" + user.getEmail())
                .content(objectMapper.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void testEditUserWithEmailNotExisting() throws Exception {
        when(userService.editUser(anyString(), any(User.class))).thenThrow(new UserNotFoundException("User does not exist"));

        mockMvc.perform(put("/users/" + getUser().getEmail())
                .content(objectMapper.writeValueAsString(modelMapper.map(getUser(), UserDTO.class)))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private User getUser() {
        return new User("id", "Peter", "Beleganski", "peter@email.com",
                LocalDate.of(1998, 3,10));
    }
}