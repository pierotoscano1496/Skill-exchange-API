package com.main.skillexchangeapi.app.config;

import com.main.skillexchangeapi.app.security.TokenUtils;
import com.main.skillexchangeapi.domain.abstractions.services.ITokenBlackList;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Autowired
    private ITokenBlackList tokenBlackList;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private Environment environment;

    private boolean isDev() {
        return Arrays.asList(environment.getActiveProfiles()).contains("dev");
    }

    private boolean isProd() {
        return Arrays.asList(environment.getActiveProfiles()).contains("prod");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/chatting", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        String[] allowedOrigins = isProd() ? new String[] { "https://tuchambita.com" }
                : isDev() ? new String[] { "http://localhost:*", "http://127.0.0.1:*" } : new String[] {};

        registry.addEndpoint("/messaging-socket")
                .setAllowedOriginPatterns(allowedOrigins)
                // .setAllowedOriginPatterns("http://localhost:3000")
                .withSockJS();
    }

    /*
     * @Override
     * public void configureClientInboundChannel(ChannelRegistration registration) {
     * registration.interceptors(new ChannelInterceptor() {
     * 
     * @Override
     * public Message<?> preSend(org.springframework.messaging.Message<?> message,
     * org.springframework.messaging.MessageChannel channel) {
     * var accessor = MessageHeaderAccessor.getAccessor(message,
     * StompHeaderAccessor.class);
     * if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
     * var authHeaders = accessor.getNativeHeader("Authorization");
     * if (authHeaders != null && !authHeaders.isEmpty()) {
     * String token = authHeaders.get(0).replace("Bearer ", "");
     * if (tokenUtils.validateToken(token) && !tokenBlackList.isBlacklisted(token))
     * {
     * UsernamePasswordAuthenticationToken user =
     * tokenUtils.getAuthentication(token);
     * accessor.setUser(user);
     * }
     * }
     * }
     * return message;
     * }
     * });
     * }
     */
}
