package com.user.managment.solution.service;

import com.user.managment.solution.model.User;
import com.user.managment.solution.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;


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
        when(userRepository.save(any())).thenReturn(new User());
        userService.saveUser(new User());

        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void findUserById() {
    }

    @Test
    public void findByEmail() {
    }

    @Test
    public void findAllUsers() {
    }

    @Test
    public void deleteUser() {
    }

    @Test
    public void editUser() {
    }
}