package com.example.service;

import com.example.domain.Message;
import com.example.domain.User;
import com.example.domain.dto.MessageDto;
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

    public Page<MessageDto> messageList(Pageable pageable, String filter, User user) {
        if(filter != null && !filter.isEmpty()) {
            return messageRepo.findAllByTag(filter, pageable, user);
        }
        else {
            return messageRepo.findAll(pageable, user);
        }
    }

    public Page<MessageDto> messageListForUser(Pageable pageable, User author, User user) {
        return messageRepo.findByUser(pageable, user, author);
    }

    public Page<MessageDto> findAll(Pageable pageable, User user) {
        return messageRepo.findAll(pageable, user);
    }

    public void save(Message message) {
        messageRepo.save(message);
    }
}
