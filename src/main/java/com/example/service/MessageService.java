package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.entity.Message;
import com.example.entity.Account;
import com.example.repository.MessageRepository;
import com.example.repository.AccountRepository;

import java.util.Optional;
import java.util.List;

@Service
@Transactional
public class MessageService {
    MessageRepository messageRepository;
    AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository){
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().length() > 255 || message.getMessageText().isEmpty()) {
            throw new IllegalArgumentException("Invalid message");
        }

        Optional<Account> existingAccount = accountRepository.findById(message.getPostedBy());
        if(existingAccount.isPresent()) {
            return messageRepository.save(message);
        } else {
            throw new IllegalArgumentException("Invalid message");
        }
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(int messageId) {
        Optional<Message> message = messageRepository.findById(messageId);
        return message.orElse(null);
    }

    public boolean deleteMessage(int messageId) {
        if (messageRepository.existsById(messageId)) {
            messageRepository.deleteById(messageId);
            return true;
        }
        return false;
    }

    public int updateMessage(int messageId, String messageText) {
        if (messageText == null || messageText.isEmpty() || messageText.length() > 255) {
            throw new IllegalArgumentException("Invalid message");
        }

        Optional<Message> existingMessage = messageRepository.findById(messageId);
        if(existingMessage.isPresent()) {
            existingMessage.get().setMessageText(messageText);
            messageRepository.save(existingMessage.get());
            return 1;
        } else {
            throw new IllegalArgumentException("Message doesn't exist");
        }
    }

    public List<Message> getUserMessages(int accountId) {
        return messageRepository.findAllByPostedBy(accountId);
    }
}
