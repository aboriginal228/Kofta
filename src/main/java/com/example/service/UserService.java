package com.example.service;

import com.example.domain.Role;
import com.example.domain.User;
import com.example.repo.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final MailSender2 mailSender;

    public UserService(UserRepo userRepo, MailSender2 mailSender) {
        this.userRepo = userRepo;
        this.mailSender = mailSender;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepo.findAllByUsername(s);
    }

    public boolean addUser(User user) {
        User userFromDb = userRepo.findAllByUsername(user.getUsername());
        if(userFromDb != null) return false;

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());

        userRepo.save(user);

        if(!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s \n" +
                            "Welcome to Kofta. Please, visit next link: http://localhost:8080/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );
            mailSender.send(user.getEmail(), "Activation Code", message);
        }
        return true;
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);
        if(user == null) return false;
        user.setActive(true);
        user.setActivationCode(null);
        userRepo.save(user);
        return true;
    }
}
