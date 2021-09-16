package learn.example.springchat.controller;

import learn.example.springchat.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private static final String TOPIC = "/topic/public";

    @SendTo(TOPIC)
    @MessageMapping("/chat.sendMessage")
    public ChatMessage setMessage(@Payload ChatMessage chatMessage) {
        return chatMessage;
    }

    @SendTo(TOPIC)
    @MessageMapping("/chat.addUser")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor accessor) {
        accessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

}
