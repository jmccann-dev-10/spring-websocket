package learn.example.springchat.controller;

import learn.example.springchat.model.ChatMessage;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

//    private static final String TOPIC = "/topic/public";

    @SendTo("/topic/{chatId}")
    @MessageMapping("{chatId}/chat.sendMessage")
    public ChatMessage setMessage(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor accessor) {
        chatMessage.setSender(accessor.getUser().getName());
        return chatMessage;
    }

    @SendTo("/topic/{topicName}")
    @MessageMapping("{topicName}/chat.addUser")
    public ChatMessage addUser(@DestinationVariable("topicName") String topicName, @Payload ChatMessage chatMessage, SimpMessageHeaderAccessor accessor) {
        accessor.getSessionAttributes().put("topicName", topicName);
        chatMessage.setSender(accessor.getUser().getName());
        chatMessage.setContent(accessor.getUser().getName() + " joined " + topicName + "!");
        return chatMessage;
    }

}
