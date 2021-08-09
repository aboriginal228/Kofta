package com.example;

import com.example.domain.Role;
import org.junit.jupiter.api.Test;

public class Test1 {

    @Test
    void getUser() {
        System.out.println(Role.USER.name());
    }
}
