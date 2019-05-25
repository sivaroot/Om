package com.backend.Om.controller;

import com.backend.Om.model.*;
import com.backend.Om.model.ChatMessage.MessageType;
import com.backend.Om.repository.ContentRepository;
import com.backend.Om.repository.LobbyRepository;
import com.backend.Om.repository.UserLobbyRepository;
import com.backend.Om.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import static java.lang.String.format;

@RestController
@CrossOrigin("*")
public class ChatController {

  private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

  @Autowired
  private SimpMessageSendingOperations messagingTemplate;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private LobbyRepository lobbyRepository;

  @Autowired
  private UserLobbyRepository userLobbyRepository;

  @Autowired
  private ContentRepository contentRepository;

  @MessageMapping("/chat/{roomId}/sendMessage")
  public void sendMessage(@DestinationVariable String roomId, @Payload ChatMessage chatMessage) {
    Lobby lobby = lobbyRepository.findLobbyByLobbyName(roomId);
    User user = userRepository.findUserByNickname(chatMessage.getSender());

    chatMessage.setMessageTime(ZonedDateTime.now());
    Content content = new Content(user,lobby,chatMessage.getMessageTime(),chatMessage.getContent());

    contentRepository.save(content);

    messagingTemplate.convertAndSend(format("/channel/%s", roomId), chatMessage);
  }

  @MessageMapping("/chat/{roomId}/addUser")
  public void addUser(@DestinationVariable String roomId, @Payload ChatMessage chatMessage,
    SimpMessageHeaderAccessor headerAccessor) {
    String currentRoomId = (String) headerAccessor.getSessionAttributes().put("room_id", roomId);
    if (currentRoomId != null) {
      ChatMessage leaveMessage = new ChatMessage();
      leaveMessage.setType(MessageType.LEAVE);
      leaveMessage.setSender(chatMessage.getSender());
      leaveMessage.setMessageTime(ZonedDateTime.now());
      messagingTemplate.convertAndSend(format("/channel/%s", currentRoomId), leaveMessage);
    }
    if(!userLobbyRepository.existsUserLobbyByLobby_LobbyNameAndAndUser_Nickname(roomId,chatMessage.getSender())) {

      Lobby lobby;
      if (lobbyRepository.existsLobbyByLobbyName(roomId))
        lobby = lobbyRepository.findLobbyByLobbyName(roomId);
      else
        lobby = lobbyRepository.save(new Lobby(roomId));

      User user;
      if (userRepository.existsUserByNickname(chatMessage.getSender()))
        user = userRepository.findUserByNickname(chatMessage.getSender());
      else
        user = userRepository.save(new User(chatMessage.getSender()));

      userLobbyRepository.save(new UserLobby(user,lobby));

    }

    UserLobby userLobby = userLobbyRepository.findUserLobbyByUser_NicknameAndLobby_LobbyName(chatMessage.getSender(),roomId);
    userLobby.setActive(true);
    userLobbyRepository.save(userLobby);

    headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
    chatMessage.setMessageTime(ZonedDateTime.now());
    messagingTemplate.convertAndSend(format("/channel/%s", roomId), chatMessage);
  }

  @GetMapping("/content/{lobby}")
  public List<Content> getContentByLobbyName(@PathVariable String lobby){
    return contentRepository.findContentsByLobby_LobbyNameOrderByMessageTime(lobby);
  }
  @GetMapping("/users/{lobby}")
  public List<UserLobby> getUsersInLobby(@PathVariable String lobby){
    return userLobbyRepository.findUserLobbiesByLobby_LobbyNameOrderByActive(lobby);
  }
}
