package com.example.service;

import com.example.domain.Role;
import com.example.domain.User;
import com.example.repo.UserRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepo userRepo;
    private final MailSend mailSend;
    private final PasswordEncoder passwordEncoder;

    @Value("${hostname}")
    private String hostname;

    public UserService(UserRepo userRepo, MailSend mailSend, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.mailSend = mailSend;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepo.findAllByUsername(s);
        if(user == null) {
            throw new LockedException("User not found");
        }
        if (user.getActivationCode() != null ) {
            throw new LockedException("Email not activated");
        }
        return user;
    }

    public boolean addUser(User user) {
        User userFromDb = userRepo.findAllByUsername(user.getUsername());
        if(userFromDb != null) return false;

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);

        sendMessage(user);
        return true;
    }

    private void sendMessage(User user) {
        if(!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s \n" +
                            "Welcome to Kofta. Please, visit next link: http://%s/activate/%s",
                    user.getUsername(),
                    hostname,
                    user.getActivationCode()
            );
            mailSend.send(user.getEmail(), "Activation Code", message);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);
        if(user == null) return false;
        user.setActive(true);
        user.setActivationCode(null);
        userRepo.save(user);
        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) {
        Set<String> roles = Arrays.stream(Role.values())
                .map(Enum::name)
                .collect(Collectors.toSet());
        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) user.getRoles().add(Role.valueOf(key));
        }
        System.out.println(form.keySet());
        user.setUsername(username);
        userRepo.save(user);
    }

    public void updateProfile(User user, String password, String email) {
        String userEmail = user.getEmail();

        boolean isEmailChanged = (userEmail != null && !userEmail.equals(email) || email != null && !email.equals(userEmail));

        if (isEmailChanged) {
            user.setEmail(email);

            if(!StringUtils.isEmpty(email)) {
                user.setActivationCode(UUID.randomUUID().toString());
            }
        }

        if(!StringUtils.isEmpty(password)) {
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepo.save(user);

        if (isEmailChanged) sendMessage(user);
    }

    public void subscribe(User currentUser, User user) {
        user.getSubscribers().add(currentUser);
        userRepo.save(user);
    }

    public void unsubscribe(User currentUser, User user) {
        user.getSubscribers().remove(currentUser);
        userRepo.save(user);
    }
}
