package learn.example.springchat.config.interceptor;

import learn.example.springchat.model.AppUser;
import learn.example.springchat.model.ChatMessage;
import learn.example.springchat.util.JwtConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Objects;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    private  final JwtConverter converter;

    public JwtChannelInterceptor(JwtConverter converter) {
        this.converter = converter;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            parseAuthorizationHeader(accessor);
        }
        return message;
    }

    private void parseAuthorizationHeader(StompHeaderAccessor accessor) {
        String authorization = accessor.getFirstNativeHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer")) {
            AppUser user = converter.getUserFromToken(authorization.substring(7));
            if (user != null) {
                Authentication auth = new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
                accessor.setUser(auth);
            }
        }
    }
}
