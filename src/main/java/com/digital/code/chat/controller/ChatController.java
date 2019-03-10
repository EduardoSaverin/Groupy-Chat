package com.digital.code.chat.controller;

import com.digital.code.chat.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    // Message with destination /app/chat.message will be routed to this method
    @MessageMapping("/chat.message")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){
        System.out.println("->"+chatMessage.toString());
        return chatMessage;
    }

    // Message with destination /app/chat.user will be routed to this method
    @MessageMapping("/chat.user")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor simpMessageHeaderAccessor){
        System.out.println("-> User"+chatMessage.getSender());
        simpMessageHeaderAccessor.getSessionAttributes().put("username",chatMessage.getSender());
        return chatMessage;
    }
}
