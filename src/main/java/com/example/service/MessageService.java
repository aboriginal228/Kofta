package com.example.service;

import com.example.domain.Message;
import com.example.domain.User;
import com.example.repo.MessageRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private MessageRepo messageRepo;

    public MessageService(MessageRepo messageRepo) {
        this.messageRepo = messageRepo;
    }

    public Page<Message> messageList(Pageable pageable, String filter) {
        if(filter != null && !filter.isEmpty()) {
            return messageRepo.findAllByTag(filter, pageable);
        }
        else {
            return messageRepo.findAll(pageable);
        }
    }

    public Page<Message> messageListForUser(Pageable pageable, User author) {
        return messageRepo.findByUser(pageable, author);
    }
}
