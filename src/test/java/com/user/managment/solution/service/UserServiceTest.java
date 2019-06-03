package com.user.managment.solution.service;

import com.user.managment.solution.exception.UserNotFoundException;
import com.user.managment.solution.model.User;
import com.user.managment.solution.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @Before
    public void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    public void saveUser() {
        User user = getUser();
        when(userRepository.save(any())).thenReturn(user);
        User savedUser = userService.saveUser(user);

        verify(userRepository, times(1)).save(any());
        assertEquals(user, savedUser);
    }

    @Test
    public void findByEmail() throws UserNotFoundException {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(getUser()));
        User user = userService.findByEmail("email@email.com");

        verify(userRepository, times(1)).findByEmail(anyString());
        assertEquals(getUser(), user);
    }

    @Test(expected = UserNotFoundException.class)
    public void findByEmailNotExisting() throws UserNotFoundException {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        userService.findByEmail("email@email.com");

        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    public void findAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(getUser()));

        List<User> allUsers = userService.findAllUsers();
        verify(userRepository, times(1)).findAll();
        assertEquals(1, allUsers.size());
        assertEquals(getUser(), allUsers.get(0));
    }

    @Test
    public void deleteUser() throws UserNotFoundException {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(getUser()));

        userService.deleteUser("email@email.com");

        verify(userRepository, times(1)).deleteById(anyString());
    }

    @Test(expected = UserNotFoundException.class)
    public void deleteNotExistingUser() throws UserNotFoundException {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(getUser()));

        userService.deleteUser("sl@a.com");

        verify(userRepository, times(0)).deleteById(anyString());
    }

    @Test
    public void editUser() throws UserNotFoundException {
        User user = getUser();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(userRepository.save(any())).thenReturn(user);

        User out = userService.editUser("email@email.com", getUser());
        assertEquals(user, out);
    }

    @Test(expected = UserNotFoundException.class)
    public void editNonExistingUser() throws UserNotFoundException {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        userService.editUser("asd@asd.com", getUser());

        verify(userRepository, times(0)).save(any());
    }

    private User getUser() {
        User user = new User();
        user.setId("userId");
        user.setEmail("email@email.com");
        user.setFirstName("peter");
        user.setLastName("beleganski");
        user.setDateOfBirth(LocalDate.of(2005, 10, 1));
        return user;
    }
}