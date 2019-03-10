package com.digital.code.chat;

import com.digital.code.chat.model.ChatMessage;
import com.digital.code.chat.model.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class Listener {

    private static final Logger logger = LoggerFactory.getLogger(Listener.class);

    @Autowired
    SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void websocketConnectListener(SessionConnectEvent connectedEvent) {
        logger.info("Connected");
    }

    @EventListener
    public void websocketDisconnectListener(SessionDisconnectEvent disconnectEvent){
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(disconnectEvent.getMessage());
        String username = (String)headerAccessor.getSessionAttributes().get("username");
        if(username != null){
            logger.info("Disconnected " + username);
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(MessageType.LEAVE);
            chatMessage.setSender(username);
            messagingTemplate.convertAndSend("/topic/public",chatMessage);
        }
    }
}
