package com.example.repo;

import com.example.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findAllByUsername(String username);

    User findByActivationCode(String code);
}
