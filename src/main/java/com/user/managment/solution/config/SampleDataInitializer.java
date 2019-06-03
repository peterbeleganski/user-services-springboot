package com.user.managment.solution.config;

import com.user.managment.solution.model.User;
import com.user.managment.solution.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


@Component
public class SampleDataInitializer {

    private static final Logger log = LoggerFactory.getLogger(SampleDataInitializer.class);

    private final UserRepository userRepository;

    public SampleDataInitializer(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {
        if (userRepository.findAll().isEmpty()) {
            User peter = new User(null, "Peter", "Beleganski", "peter@beleganski.com", LocalDate.of(1998,3,10));
            User george = new User(null, "George", "Georgiev", "George@Georgiev.com", LocalDate.of(1972,10,5));

            List<User> users = Arrays.asList(peter, george);

            log.info("Saving initial users into db: {}" , users);
            userRepository.saveAll(users);
        }
    }
}