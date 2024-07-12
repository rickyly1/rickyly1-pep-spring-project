package com.example.controller;

import com.example.entity.*;
import com.example.service.*;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private MessageService messageService;

    // 1 - register account + error handling
    @PostMapping("/register")
    public Account registerAccount(@RequestBody Account account) {
        return accountService.registerAccount(account);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleIllegalStateException(IllegalStateException e) {
        return e.getMessage();
    }

    // 2 - login validation + error handling
    @PostMapping("/login")
    public Account loginAccount(@RequestBody Account account) throws AuthenticationException {
        return accountService.loginAccount(account);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleAuthenticationException(AuthenticationException e) {
        return e.getMessage();
    }

    // 3 - create message + error handling
    @PostMapping("/messages")
    public Message messageAccount(@RequestBody Message message) {
        return messageService.createMessage(message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        return e.getMessage();
    }

    // 4 - get all messages, no error handling required (always 200)
    @GetMapping("/messages")
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    // 5 - get a message by id
    @GetMapping("/messages/{messageId}")
    public Message getMessageById(@PathVariable int messageId) {
        return messageService.getMessageById(messageId);
    }

    // 6 - delete a message
    @DeleteMapping("/messages/{messageId}")
    public Integer deleteMessage(@PathVariable int messageId) {
        boolean deleted = messageService.deleteMessage(messageId);
        if (deleted == true) {
            return 1;
        }
        return null;
    }

    // 7 - update a message text, use IllegalArgumentException for 400 (implemented above)
    @PatchMapping("/messages/{messageId}")
    public @ResponseBody ResponseEntity<Object> updateMessage(@PathVariable int messageId, @RequestBody Message messageText) {
        //try {
            return messageService.updateMessage(messageId, messageText.getMessageText());
            //return rowsUpdated;
        // } catch (IllegalArgumentException e) {
        //     throw e; 
        // }
    }

    // 8 - retrieve all messages from a user
    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getUserMessages(@PathVariable int accountId) {
        return messageService.getUserMessages(accountId);
    }
}
