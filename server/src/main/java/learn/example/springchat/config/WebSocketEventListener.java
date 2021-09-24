package learn.example.springchat.config;

import learn.example.springchat.model.AppUser;
import learn.example.springchat.model.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Configuration
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    private final SimpMessageSendingOperations messageTemplate;

    public WebSocketEventListener(SimpMessageSendingOperations messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String topicName = (String) accessor.getSessionAttributes().get("topicName");
        logger.info("User Disconnected: " + accessor.getUser().getName() + "\n\tTopic: " + topicName);

        ChatMessage message = new ChatMessage();
        message.setType(ChatMessage.MessageType.LEAVE);
        message.setSender(accessor.getUser().getName());
        message.setContent(accessor.getUser().getName() + " left " + topicName + "!");

        messageTemplate.convertAndSend("/topic/" + topicName, message);
    }
}
