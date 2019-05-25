package com.backend.Om.controller;

import static java.lang.String.format;

import com.backend.Om.model.ChatMessage;
import com.backend.Om.model.ChatMessage.MessageType;
import com.backend.Om.model.UserLobby;
import com.backend.Om.repository.UserLobbyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

  private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

  @Autowired
  private SimpMessageSendingOperations messagingTemplate;

  @Autowired
  private UserLobbyRepository userLobbyRepository;
  @EventListener
  public void handleWebSocketConnectListener(SessionConnectedEvent event) {
    logger.info("Received a new web socket connection.");
  }

  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

    String username = (String) headerAccessor.getSessionAttributes().get("username");
    String roomId = (String) headerAccessor.getSessionAttributes().get("room_id");
    if (username != null) {
      logger.info("User Disconnected: " + username);

      ChatMessage chatMessage = new ChatMessage();
      chatMessage.setType(MessageType.LEAVE);
      chatMessage.setSender(username);
      UserLobby userLobby = userLobbyRepository.findUserLobbyByUser_NicknameAndLobby_LobbyName(username,roomId);
      userLobby.setActive(false);
      userLobbyRepository.save(userLobby);

      messagingTemplate.convertAndSend(format("/channel/%s", roomId), chatMessage);
    }
  }
}
