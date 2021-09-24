package com.example.domain.util;

import com.example.domain.User;

public abstract class MessageHelper {

    public static String getAuthorName(User author) {
        return author != null ? author.getUsername() : "<none>";
    }
}
